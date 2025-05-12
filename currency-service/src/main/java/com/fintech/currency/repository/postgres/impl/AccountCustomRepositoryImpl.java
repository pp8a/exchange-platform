package com.fintech.currency.repository.postgres.impl;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.fintech.currency.exception.InsufficientFundsException;
import com.fintech.currency.repository.postgres.AccountCustomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AccountCustomRepositoryImpl implements AccountCustomRepository {
	private final DatabaseClient databaseClient;
	
	@Override
	public Mono<Integer> updateBalanceIfLessThanMax() {
		String sql = """
	            UPDATE account
	            SET balance = ROUND(LEAST(balance * 1.10, initial_balance * 2.07), 2),
	                updated_at = now()
	            WHERE balance < initial_balance * 2.07
	        """;

		return databaseClient.sql(sql)
	            .fetch()
	            .rowsUpdated()
	            .map(Long::intValue);
	}

	@Override
	public Mono<Void> transferBetweenUsers(UUID fromUserId, UUID toUserId, BigDecimal amount) {
		return databaseClient.inConnection(conn -> // открываем ручной доступ к соединению с БД
	        Mono.from(conn.beginTransaction()) // начинаем транзакцию
	            .then(Mono.from(conn.createStatement("""
	                SELECT balance FROM account WHERE user_id = $1 FOR UPDATE
	            """) // блокируем строку с балансом "отправителя"
	            		.bind(0, fromUserId).execute())
	            .flatMap(result -> Mono.from(result.map((row, meta) -> row.get("balance", BigDecimal.class)))))
	            .flatMap(balance -> {
	                if (balance == null || balance.compareTo(amount) < 0) {
	                	return Mono.error(new InsufficientFundsException(fromUserId, balance, amount)); // проверка средств
	                }
	                return Mono.empty(); // всё ок — продолжаем
	            })
	            .then(Mono.from(conn.createStatement("""
	                UPDATE account
	                SET balance = balance - $1, updated_at = now()
	                WHERE user_id = $2
	            """).bind(0, amount).bind(1, fromUserId).execute())
	            		.flatMap(result -> Mono.from(result.getRowsUpdated()))
	                    .doOnNext(rows -> log.info("⬇️ Deducted from sender, rows affected: {}", rows))
	                    .filter(rows -> rows > 0)
	                    .switchIfEmpty(Mono.error(new RuntimeException("❌ Sender account update failed")))
	            		)
	            .then(Mono.from(conn.createStatement("""
	                UPDATE account
	                SET balance = balance + $1, updated_at = now()
	                WHERE user_id = $2
	            """).bind(0, amount).bind(1, toUserId).execute())
	            		.flatMap(result -> Mono.from(result.getRowsUpdated()))
	                    .doOnNext(rows -> log.info("⬆️ Added to receiver, rows affected: {}", rows))
	                    .filter(rows -> rows > 0)
	                    .switchIfEmpty(Mono.error(new RuntimeException("❌ Receiver account update failed")))
	            		)
	            .then(Mono.from(conn.commitTransaction())) // коммитим транзакцию
	    ).then();// итог — Mono<Void>
	}

}

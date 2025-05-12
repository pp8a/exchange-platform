package com.fintech.currency.migration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.fintech.currency.model.mongo.ConversionEvent;
import com.fintech.currency.model.mongo.CurrencyEvent;
import com.fintech.currency.model.mongo.FeeEvent;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChangeUnit(id = "init-currency-mongo-data", order = "002", author = "user")
public class CurrencyInitialDataMigration {
	@Execution
    public void insertInitialData(ReactiveMongoTemplate mongoTemplate) {
        log.info("📥 Inserting initial data into MongoDB...");

        // ✅ Currency Events
        List<CurrencyEvent> currencyEvents = List.of(
                new CurrencyEvent(null, UUID.randomUUID().toString(), "USD", 
                		new BigDecimal("1.00"), 
                		new BigDecimal("1.05"), 
                		LocalDateTime.now()),
                new CurrencyEvent(null, UUID.randomUUID().toString(), "EUR", 
                		new BigDecimal("0.90"), 
                		new BigDecimal("0.95"), 
                		LocalDateTime.now())
        );

        // ✅ Conversion Events
        List<ConversionEvent> conversionEvents = List.of(
        	    new ConversionEvent(null, UUID.randomUUID().toString(), "client1", "USD", "EUR",
        	        new BigDecimal("100.00"), new BigDecimal("0.95"), LocalDateTime.now())
        	);

        // ✅ Fee Events
        List<FeeEvent> feeEvents = List.of(
                new FeeEvent(null, conversionEvents.get(0).getTransactionId(), 
                		new BigDecimal("0.50"), "EUR", LocalDateTime.now())
        );

        mongoTemplate.insertAll(currencyEvents).subscribe();
        mongoTemplate.insertAll(conversionEvents).subscribe();
        mongoTemplate.insertAll(feeEvents).subscribe();
    }

    @RollbackExecution
    public void rollback() {
        log.warn("❌ Rollback executed: Initial data insertion rolled back!");
    }
}

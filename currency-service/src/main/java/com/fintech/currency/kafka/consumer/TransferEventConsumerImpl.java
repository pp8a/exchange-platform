package com.fintech.currency.kafka.consumer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;

import com.fintech.currency.avro.TransferEvent;
import com.fintech.currency.model.mongo.TransferLog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferEventConsumerImpl implements TransferEventConsumer {
	
	private final ReceiverOptions<String, TransferEvent> receiverOptions;
    private final ReactiveMongoTemplate mongoTemplate;

	@Override
	public Mono<Void> consume() {
		return KafkaReceiver.create(receiverOptions)
                .receive()
                .flatMap(record -> {
                    TransferEvent event = record.value();
                    log.info("📥 Received TransferEvent: {}", event);
                    TransferLog logEntry = new TransferLog(
                            null,
                            event.getFromUserId().toString(),
                            event.getToUserId().toString(),
                            new BigDecimal(event.getAmount().toString()),
                            LocalDateTime.parse(event.getTimestamp().toString())
                        );
                    return mongoTemplate.save(logEntry, "transfer_logs")
                            .doOnSuccess(r -> log.info("✅ Saved to MongoDB"));
                })
                .doOnError(err -> log.error("❌ Kafka consumer error", err))
                .then();
    	
	}

}

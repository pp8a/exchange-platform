package com.fintech.currency.kafka;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.fintech.currency.kafka.consumer.TransferEventConsumer;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferEventConsumerRunner implements ApplicationRunner {
	private final TransferEventConsumer consumer;	
	private Disposable subscription;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("🚀 Starting TransferEventConsumer...");
	    subscription = consumer.consume().subscribe(
	        null,
	        error -> log.error("❌ TransferEventConsumer failed", error),
	        () -> log.warn("⚠️ TransferEventConsumer stream completed unexpectedly")
	    );
	}
	
	@PreDestroy
    public void shutdown() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
            log.info("🛑 Kafka consumer gracefully stopped.");
        }
    }
}

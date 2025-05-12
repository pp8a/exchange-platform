package com.fintech.currency.kafka.consumer;

import reactor.core.publisher.Mono;

public interface TransferEventConsumer {
	Mono<Void> consume();
}

package com.fintech.currency.kafka.producer;

import java.util.UUID;

import com.fintech.currency.avro.TransferEvent;
import com.fintech.currency.avro.UserEvent;

import reactor.core.publisher.Mono;

public interface UserKafkaProducer {
    Mono<Void> sendEvent(UUID key, UserEvent event);
    Mono<Void> sendEvent(String key, TransferEvent event);
}

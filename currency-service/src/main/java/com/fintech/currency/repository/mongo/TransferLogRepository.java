package com.fintech.currency.repository.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fintech.currency.model.mongo.TransferLog;

import reactor.core.publisher.Flux;

public interface TransferLogRepository extends ReactiveMongoRepository<TransferLog, String> {
    Flux<TransferLog> findAllByFromUserIdOrToUserIdOrderByTimestampDesc(String fromUserId, String toUserId);
}
package com.fintech.currency.migration;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import org.springframework.data.domain.Sort;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChangeUnit(id = "transfer-events-migration", order = "003", author = "user")
public class TransferEventsMigration {
	@Execution
    public void createTransferEventsCollection(ReactiveMongoTemplate mongoTemplate) {
        log.info("📦 Creating collection: transfer_events");

        String collectionName = "transfer_events";

        mongoTemplate.collectionExists(collectionName)
            .flatMap(exists -> exists ? Mono.empty() : mongoTemplate.createCollection(collectionName))
            .doOnSuccess(__ -> log.info("✅ Collection created: {}", collectionName))
            .subscribe();

        mongoTemplate.indexOps(collectionName)
            .ensureIndex(new Index().on("fromUserId", Sort.Direction.ASC))
            .subscribe();

        mongoTemplate.indexOps(collectionName)
            .ensureIndex(new Index().on("toUserId", Sort.Direction.ASC))
            .subscribe();

        mongoTemplate.indexOps(collectionName)
            .ensureIndex(new Index().on("timestamp", Sort.Direction.DESC))
            .subscribe();
    }
	
	@RollbackExecution
    public void rollback() {
        log.warn("❌ Rollback executed: transfer_events migration");
    }
}

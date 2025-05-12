package com.fintech.currency.migration;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import java.util.Map;

import org.springframework.data.domain.Sort;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChangeUnit(id = "currency-events-migration", order = "001", author = "user")
public class MongoMigration {
	
	public MongoMigration() {
        log.info("✅ MongoMigration loaded!");
    }
	
	@Execution
	public void createCollections(ReactiveMongoTemplate mongoTemplate) {
		log.info("🔄 Running MongoDB migrations...");		
			
		Map<String, String> collections = Map.of(
			    "currency_events", "eventId",
			    "conversion_events", "transactionId",
			    "fee_events", "transactionId"
			);
		
		collections.forEach((collectionName, indexKey) -> {
		    mongoTemplate.collectionExists(collectionName)
		        .flatMap(exists -> exists ? Mono.empty() : mongoTemplate.createCollection(collectionName))
		        .subscribe();

		    mongoTemplate.indexOps(collectionName)
		        .ensureIndex(new Index().on(indexKey, Sort.Direction.ASC).unique())
		        .subscribe();

		    mongoTemplate.indexOps(collectionName)
		        .ensureIndex(new Index().on("timestamp", Sort.Direction.DESC))
		        .subscribe();
		});	    

	}	
	
	@RollbackExecution
	public void rollback() {
	    log.warn("❌ Rollback executed: Migration rollback triggered!");
	}

}

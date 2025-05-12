package com.fintech.currency.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.mongodb.client.MongoClients; 
import com.mongodb.client.MongoClient; 

import io.mongock.runner.springboot.EnableMongock;
import io.mongock.runner.springboot.base.MongockApplicationRunner;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.driver.mongodb.springdata.v4.SpringDataMongoV4Driver;

@Configuration
@EnableMongock
public class MongoMigrationsConfig {
	
	@Value("${spring.data.mongodb.uri}") 
    private String mongoUri;
    
    @Bean
    MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }
    
    @Bean
    com.mongodb.reactivestreams.client.MongoClient reactiveMongoClient() {
        return com.mongodb.reactivestreams.client.MongoClients.create(mongoUri);
    }    

    @Bean
    MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient, "currency_events"));
    }
    
    @Bean
    ReactiveMongoTemplate reactiveMongoTemplate(com.mongodb.reactivestreams.client.MongoClient reactiveMongoClient) {
        return new ReactiveMongoTemplate(reactiveMongoClient, "currency_events");
    }
    
    @Bean
    MongockApplicationRunner mongockRunner(ApplicationContext applicationContext, MongoTemplate mongoTemplate) {
        return MongockSpringboot.builder()
                .setDriver(SpringDataMongoV4Driver.withDefaultLock(mongoTemplate))
                .setSpringContext(applicationContext)
                .addMigrationScanPackage("com.fintech.currency.migration")
                .buildApplicationRunner();
    }
}

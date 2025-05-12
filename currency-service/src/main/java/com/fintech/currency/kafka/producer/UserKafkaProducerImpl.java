package com.fintech.currency.kafka.producer;

import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fintech.currency.avro.TransferEvent;
import com.fintech.currency.avro.UserEvent;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Component
@RequiredArgsConstructor
public class UserKafkaProducerImpl implements UserKafkaProducer {
	@Qualifier("userKafkaSender")
	private final KafkaSender<UUID, UserEvent> kafkaSender;
	@Qualifier("transferKafkaSender")
    private final KafkaSender<String, TransferEvent> transferKafkaSender;
	
    private final String topic = "user-events";
    private final String transferTopic = "transfer-events";

    @Override
    public Mono<Void> sendEvent(UUID key, UserEvent event) {
        return send(key, event, topic, kafkaSender);
    }

    @Override
    public Mono<Void> sendEvent(String key, TransferEvent event) {
        return send(key, event, transferTopic, transferKafkaSender);
    }
	
	private <K, V> Mono<Void> send(K key, V event, String topic, KafkaSender<K, V> sender) {
	    ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, event);
	    SenderRecord<K, V, K> senderRecord = SenderRecord.create(record, key);
	    return sender.send(Flux.just(senderRecord)).next().then();
	}
	
	@PreDestroy
    public void shutdown() {
        System.out.println("❌ Kafka producer shutting down...");
        kafkaSender.close();
    }
	
//	@Override
//	public Mono<Void> sendEvent(UUID key, UserEvent event) {
//		ProducerRecord<UUID, UserEvent> record = new ProducerRecord<UUID, UserEvent>(topic, key, event);
//        SenderRecord<UUID, UserEvent, UUID> senderRecord = SenderRecord.create(record, key);
//        return kafkaSender.send(Flux.just(senderRecord))
//                .next()
//                .then();// Возвращаем Mono<Void>
//	}
//	
//	@Override
//	public Mono<Void> sendEvent(String key, TransferEvent event) {
//		ProducerRecord<String, TransferEvent> record = new ProducerRecord<>(transferTopic, key, event);
//	    SenderRecord<String, TransferEvent, String> senderRecord = SenderRecord.create(record, key);
//	    return transferKafkaSender.send(Flux.just(senderRecord))
//	            .next()
//	            .then(); // Mono<Void>
//	}
	

}

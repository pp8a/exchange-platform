package com.fintech.currency.kafka.producer;

import io.confluent.kafka.serializers.KafkaAvroSerializer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.apache.kafka.common.serialization.StringSerializer;
import com.fintech.currency.avro.TransferEvent;
import com.fintech.currency.avro.UserEvent;

import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class KafkaSenderConfig {
	@Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;
    
    private Map<String, Object> commonProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put("schema.registry.url", schemaRegistryUrl);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 5);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        return props;
    }
    
    @Bean(name = "userKafkaSender")
    KafkaSender<UUID, UserEvent> userKafkaSender() {
    	Map<String, Object> props = new HashMap<>(commonProps());        

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);   
        
        SenderOptions<UUID, UserEvent> senderOptions = SenderOptions.create(props);
        return KafkaSender.create(senderOptions);
    }
    
    @Bean(name = "transferKafkaSender")
    KafkaSender<String, TransferEvent> transferKafkaSender() {
    	Map<String, Object> props = new HashMap<>(commonProps());        

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);        

        SenderOptions<String, TransferEvent> senderOptions = SenderOptions.create(props);
        return KafkaSender.create(senderOptions);
    }

}

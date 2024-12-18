package com.guner.kafka.config;

import com.guner.kafka.model.Greeting;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers:http://localhost:9092}")
    private String bootstrapAddress;

    private static final String GROUP_ID = "groupId-1";

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                GROUP_ID);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    // https://www.baeldung.com/spring-retry-kafka-consumer this method is blocking retry
    @Bean
    public DefaultErrorHandler errorHandler() {
        long interval = 60000L;
        int maxAttempts= 3;
        BackOff fixedBackOff = new FixedBackOff(interval, maxAttempts);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, e) -> {
            // logic to execute when all the retry attemps are exhausted
        }, fixedBackOff);
        //errorHandler.addRetryableExceptions(SocketTimeoutException.class);
        //errorHandler.addRetryableExceptions(RuntimeException.class);
        //errorHandler.addNotRetryableExceptions(NullPointerException.class);
        return errorHandler;
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(errorHandler());

        return factory;
    }

}
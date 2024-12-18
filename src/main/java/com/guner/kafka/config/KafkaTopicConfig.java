package com.guner.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    public static final String TOPIC_NAME = "topic-1";
    public static final String TOPIC_NAME_2 = "topic-2";
    public static final String TOPIC_NAME_3 = "topic-3";

    @Value(value = "${spring.kafka.bootstrap-servers:http://localhost:9092}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic(TOPIC_NAME, 1, (short) 1);
    }

    @Bean
    public NewTopic topic2() {
        return new NewTopic(TOPIC_NAME_2, 1, (short) 1);
    }

    @Bean
    public NewTopic topic3() {
        return new NewTopic(TOPIC_NAME_3, 1, (short) 1);
    }

}
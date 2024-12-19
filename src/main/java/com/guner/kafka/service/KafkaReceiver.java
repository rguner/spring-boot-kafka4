package com.guner.kafka.service;

import com.guner.kafka.config.KafkaTopicConfig;
import com.guner.kafka.model.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;

@Service
@Slf4j
public class KafkaReceiver {

    // retry mechanism on consumer config
    // https://www.baeldung.com/spring-retry-kafka-consumer this method is blocking retry
    // if it is decided to retrying one message, all messages on topic wait during interval...
    // DeadLetterPublishingRecoverer should be used for sending message to dlt topic
    @KafkaListener(topics = "topic-1", groupId = "group-r")
    public void listenAndRetryIfRequired(String message) {
        log.info("-----   Received Message in group group-2: {}", message);
        if (message.startsWith("THE_MESSAGE_WHICH_WILL_BE_RETRY")) {
            throw new RuntimeException("Receive Exception to test retry mechanism");
        }
    }

    // for DLT
    // Log metadata (e.g., headers, exception stack trace).
    // Reprocess or Archive
    // Attempt to reprocess the message after resolving the root issue.
    // If reprocessing is not possible, archive the message for manual intervention.
    // Use tools like Prometheus, Grafana, or Kafka UI tools to monitor the DLT topic size and message consumption status.


    // this listener is optional
    @KafkaListener(topics = "topic-1-dlt", groupId = "dlt-group-id")
    public void processDeadLetter(String message) {
        log.info("Processing message from DLT: {}", message);

        // Add your custom logic to handle the message
        /*
        try {
            // Attempt to reprocess or log the error
            System.out.println("Reprocessing message: " + message);
            // Re-publish or handle the message as needed
            //republishMessage(message, KafkaTopicConfig.TOPIC_NAME);
        } catch (Exception e) {
            System.err.println("Failed to process DLT message: " + e.getMessage());
        }
         */
    }

    /*
    public void republishMessage(String message, String topic) {
        kafkaTemplate.send(topic, message);
        System.out.println("Message republished to topic: " + topic);
    }
     */

}

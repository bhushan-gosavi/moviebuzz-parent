package com.moviebuzz.kafka.config.topics;

import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import com.moviebuzz.kafka.constant.Constants;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class BookingConfirmationProducer {
    @Autowired
    private KafkaProperties kafkaProperties;

    @Value("${kafka.topics.bookingConfirmation.partitions}")
    private Integer bookingConfirmationPartitions;

    @Value("${kafka.topics.bookingConfirmation.replicas}")
    private Short bookingConfirmationReplicas;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props =
                new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic bookingConfirmationTopic() {
        return new NewTopic(Constants.BOOKING_CONFIRMATION, bookingConfirmationPartitions, bookingConfirmationReplicas);
    }
}

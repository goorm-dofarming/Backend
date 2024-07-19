package goorm.dofarming.global.config.kafka;

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {

        JsonDeserializer<String> deserializer = new JsonDeserializer<>(String.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");

        ErrorHandlingDeserializer<String> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(deserializer);

        Map<String, Object> consumerConfiguration =
                ImmutableMap.<String, Object>builder()
                        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKER)
                        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, errorHandlingDeserializer)
                        .put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, deserializer.getClass().getName())
                        .put(JsonDeserializer.TRUSTED_PACKAGES, "*")
                        .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
                        .build();


        return new DefaultKafkaConsumerFactory<>(consumerConfiguration, new StringDeserializer(), errorHandlingDeserializer);
    }
}

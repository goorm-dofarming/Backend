package goorm.chat.config;

import java.util.List;
import java.util.UUID;

public class KafkaConstants {
    private static String name = UUID.randomUUID().toString();
    public static final String KAFKA_TOPIC = "chat-log";
    public static final String GROUP_ID = name;
    public static final String KAFKA_BROKER = "172.31.7.189:9092,172.31.8.251:9092,172.31.13.117:9092";
}

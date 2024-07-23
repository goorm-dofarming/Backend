package goorm.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.notification.config.kafka.KafkaConstants;
import goorm.notification.dto.AlarmDto;
import goorm.notification.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmitterRepository emitterRepository;
    private final ObjectMapper mapper;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    public SseEmitter subscribe(Long userId, String lastEventId) {

        String emitterId = userId + "_" + UUID.randomUUID();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteByEmitterId(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteByEmitterId(emitterId));

        sendToClient(emitter, emitterId, "EventStream Created. [userId=" + userId + "]");

        if (!lastEventId.isEmpty()) {
            Map<String, SseEmitter> events = emitterRepository.findAllEmitterStartWithByUserId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
        return emitter;
    }

    public void send(Long userId, Object data) {
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByUserId(String.valueOf(userId));
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, data);
                    sendToClient(emitter, key, data);
                }
        );

    }

    @KafkaListener(topics = KafkaConstants.KAFKA_TOPIC, groupId = "alarm-group")
    public void sendAlarm(String message) throws IOException {
        AlarmDto alarmDto = mapper.readValue(message, AlarmDto.class);

        send(alarmDto.receiveId(), "alarm");
    }

    private void sendToClient(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteByEmitterId(emitterId);
            throw new RuntimeException(exception);
        }
    }
}

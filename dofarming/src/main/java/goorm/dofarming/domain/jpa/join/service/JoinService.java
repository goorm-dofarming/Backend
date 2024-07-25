package goorm.dofarming.domain.jpa.join.service;

import goorm.dofarming.domain.jpa.join.dto.request.WatermarkRequest;
import goorm.dofarming.domain.jpa.join.entity.Join;
import goorm.dofarming.domain.jpa.join.repository.JoinRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JoinService {

    private final JoinRepository joinRepository;

    @Transactional
    public void updateWatermark(Long userId, WatermarkRequest watermarkRequest) {
        Join join = joinRepository.findByUser_UserIdAndChatroom_RoomIdAndStatus(userId, watermarkRequest.roomId(), Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "Join Not Found"));

        join.getLastReadMessageId();
    }
}

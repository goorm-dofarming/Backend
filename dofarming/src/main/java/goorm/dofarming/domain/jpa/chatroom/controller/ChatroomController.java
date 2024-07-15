package goorm.dofarming.domain.jpa.chatroom.controller;

import goorm.dofarming.domain.jpa.chatroom.dto.request.ChatroomCreateRequest;
import goorm.dofarming.domain.jpa.chatroom.service.ChatroomService;
import goorm.dofarming.domain.jpa.user.dto.request.UserSignUpRequest;
import goorm.dofarming.global.common.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ChatRoom", description = "ChatRoom 관련 API")
@RestController
@RequiredArgsConstructor
public class ChatroomController {

    private final ChatroomService chatroomService;

    @Operation(
            operationId = "ChatRoom",
            summary = "채팅방 생성 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "클라이언트의 요청을 서버가 정상적으로 처리했고 새로운 리소스가 생겼다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))
                    })
            }
    )
    @PostMapping("/chatroom")
    public ResponseEntity<Long> createChatroom(
            @Parameter @Valid @RequestBody ChatroomCreateRequest chatroomCreateRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatroomService.createRoom(chatroomCreateRequest));
    }


    @Operation(
            operationId = "ChatRoom",
            summary = "채팅방 삭제 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "클라이언트의 요청은 정상적이다. 하지만 컨텐츠를 제공하지 않는다.", content = @Content),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 채팅방입니다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @DeleteMapping("/chatroom/{roomId}")
    public ResponseEntity<Void> deleteChatroom(
            @Parameter @Valid @PathVariable Long roomId
    ) {
        chatroomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}

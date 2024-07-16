package goorm.dofarming.domain.mongo.message.controller;

import goorm.dofarming.domain.jpa.chatroom.dto.response.ChatroomResponse;
import goorm.dofarming.domain.mongo.message.dto.request.MessageSearchRequest;
import goorm.dofarming.domain.mongo.message.dto.response.MessageResponse;
import goorm.dofarming.domain.mongo.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Message", description = "Message 관련 API")
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Operation(
            operationId = "Message",
            summary = "메시지 검색 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "클라이언트의 요청을 서버가 정상적으로 처리했다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
                    })
            }
    )
    @GetMapping("/message")
    public ResponseEntity<List<MessageResponse>> getSearchMessage(
            @Parameter @RequestBody MessageSearchRequest messageSearchRequest
    ) {
        return ResponseEntity.ok().body(messageService.searchMessageList(messageSearchRequest));
    }
}

package goorm.dofarming.domain.jpa.chatroom.controller;

import goorm.dofarming.domain.jpa.chatroom.dto.request.ChatroomCreateRequest;
import goorm.dofarming.domain.jpa.chatroom.dto.request.ChatroomSearchRequest;
import goorm.dofarming.domain.jpa.chatroom.dto.response.ChatroomResponse;
import goorm.dofarming.domain.jpa.chatroom.service.ChatroomService;
import goorm.dofarming.domain.jpa.user.dto.request.UserSignUpRequest;
import goorm.dofarming.domain.jpa.user.dto.response.UserResponse;
import goorm.dofarming.global.auth.DofarmingUserDetails;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @Operation(
            operationId = "ChatRoom",
            summary = "채팅방 입장 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "클라이언트의 요청을 서버가 정상적으로 처리했고 새로운 리소스가 생겼다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "입장하지 않은 채팅방입니다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @PostMapping("/chatroom/{roomId}/join")
    public ResponseEntity<Long> joinChatroom(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter @Valid @PathVariable Long roomId
    ) {

        return ResponseEntity.status(HttpStatus.CREATED).body(chatroomService.joinRoom(user.getUserId(), roomId));
    }


    @Operation(
            operationId = "ChatRoom",
            summary = "채팅방 퇴장 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "클라이언트의 요청은 정상적이다. 하지만 컨텐츠를 제공하지 않는다.", content = @Content),
                    @ApiResponse(responseCode = "404", description = "입장한 채팅방이 아닙니다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @PostMapping("/chatroom/{roomId}/leave")
    public ResponseEntity<Void> leaveChatroom(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter @Valid @PathVariable Long roomId
    ) {
        chatroomService.leaveRoom(user.getUserId(), roomId);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            operationId = "ChatRoom",
            summary = "오픈 채팅방 검색 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "클라이언트의 요청을 서버가 정상적으로 처리했다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ChatroomResponse.class))
                    })
            }
    )
    @GetMapping("/chatroom")
    public ResponseEntity<List<ChatroomResponse>> getSearchChatroom(
            @Parameter @Valid @PathVariable ChatroomSearchRequest chatroomSearchRequest
    ) {

        return ResponseEntity.ok().body(chatroomService.searchRoomList(chatroomSearchRequest));
    }


    @Operation(
            operationId = "ChatRoom",
            summary = "내 채팅방 리스트 api 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "클라이언트의 요청을 서버가 정상적으로 처리했다.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ChatroomResponse.class))
                    })
            }
    )
    @GetMapping("/chatroom")
    public ResponseEntity<List<ChatroomResponse>> getMyChatroom(
            @AuthenticationPrincipal DofarmingUserDetails user
    ) {
        return ResponseEntity.ok().body(chatroomService.myRoomList(user.getUserId()));
    }
}

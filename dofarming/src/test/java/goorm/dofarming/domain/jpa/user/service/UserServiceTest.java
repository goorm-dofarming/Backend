package goorm.dofarming.domain.jpa.user.service;

import goorm.dofarming.domain.jpa.user.dto.request.UserModifyRequest;
import goorm.dofarming.domain.jpa.user.dto.request.UserSignUpRequest;
import goorm.dofarming.domain.jpa.user.dto.response.UserResponse;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.error.exception.CustomException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

    @AfterEach
    private void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    public void 회원_조회_가입() throws Exception {
        //given
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("leeho_@naver.com", "1234", "1234");

        //when
        Long userId = userService.createUser(userSignUpRequest);

        UserResponse findUser = userService.getUser(userId);

        //then
        assertEquals(findUser.email(), "leeho_@naver.com");
        assertNotEquals(findUser.nickname(), null);
    }

    @Test
    public void 회원_수정() throws Exception {
        //given
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("leeho_@naver.com", "1234", "1234");
        Long userId = userService.createUser(userSignUpRequest);
        UserModifyRequest userModifyRequest = new UserModifyRequest(userId, "hosung", "4321");

        //when
        UserResponse user = userService.updateUser(userModifyRequest);

        //then
        assertEquals(user.email(), "leeho_@naver.com");
        assertEquals(user.nickname(), "hosung");
    }

    @Test
    public void 회원_삭제() throws Exception {
        //given
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("leeho_@naver.com", "1234", "1234");
        Long userId = userService.createUser(userSignUpRequest);

        //when
        userService.userDelete(userId);

        CustomException e = assertThrows(CustomException.class, () -> {
            userService.getUser(userId);
        });

        //then
        assertEquals(e.getMessage(), "Resource not found");
    }
}
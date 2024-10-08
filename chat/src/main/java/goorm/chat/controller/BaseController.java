package goorm.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BaseController {

    @GetMapping("/chat/health-check")
    public String healthCheck() {
        return "OK";
    }
}

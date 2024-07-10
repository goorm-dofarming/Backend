package goorm.dofarming.domain.jpa.email.service;

import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.global.util.RandomCodeGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String serviceName;

    /* 인증 번호 Map */
    Map<String, String> map = ExpiringMap.builder()
            .expirationPolicy(ExpirationPolicy.CREATED) // 삽입된 후
            .expiration(5, TimeUnit.MINUTES) // 5분 동안 보관
            .build();

    /* 이메일 전송 */
    public void mailSend(String setFrom, String toMail, String title, String content, String emailNumber) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom); // service name
            helper.setTo(toMail); // customer email
            helper.setSubject(title); // email title
            helper.setText(content, true); // content, html: true
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomException(ErrorCode.CONFLICT, "메시지를 전송할 수 없습니다.");
        }

        // Map 에 5분 동안 이메일과 인증 코드 저장
        map.put(toMail, emailNumber);
    }

    /* 이메일 작성 */
    public void joinEmail(String email) {
        String emailNumber = RandomCodeGenerator.generateCode();
        String customerMail = email;
        String title = "도파밍 회원 가입을 위한 이메일입니다.";
        String content =
                "이메일을 인증하기 위한 절차입니다." +
                        "<br><br>" +
                        "인증 번호는 " + emailNumber + "입니다." +
                        "<br>" +
                        "회원 가입 폼에 해당 번호를 입력해주세요.";
        mailSend(serviceName, customerMail, title, content, emailNumber);
    }

    /* 인증번호 확인 */
    public Boolean checkEmailNumber(String email, String emailNumber) {
        return emailNumber.equals(map.get(email)) ? true : false;
    }
}

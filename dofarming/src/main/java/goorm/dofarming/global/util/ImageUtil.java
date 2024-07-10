package goorm.dofarming.global.util;

import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class ImageUtil {

    @Value("${image.file.path}")
    private String folder;

    public String makeFilePath (MultipartFile imageFile) {

        // 파일 이름 생성
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + imageFile.getOriginalFilename();

        // 파일 경로 생성
        String imageFilePath = folder + imageFileName;

        return imageFilePath;
    }

    public void writeImageFile(Path path, MultipartFile file) {
        try {
            Files.write(path, file.getBytes());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "이미지 파일 저장에 실패했습니다.");
        }
    }
    public void deleteImageUrl(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "이미지 파일 삭제에 실패했습니다.");
        }
    }
}

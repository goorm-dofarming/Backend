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

    private static UUID uuid = UUID.randomUUID();

    @Value("${image.file.path}")
    private String folder;

    public String getImageUrl(String root, MultipartFile multipartFile) {
        return root + uuid + "_" + multipartFile.getOriginalFilename();
    }

    public Path makeFilePath (String fileName) {
        // 파일 경로 생성
        return Paths.get(folder + fileName);
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

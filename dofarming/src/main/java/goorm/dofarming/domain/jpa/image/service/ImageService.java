package goorm.dofarming.domain.jpa.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.domain.jpa.image.repository.ImageRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {
        // 파일 이름이 너무 길어지는 것을 방지하기 위해서 UUID는 3자리까지만 받아온다.
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 3);
        String fileName = uuid + "_" + file.getOriginalFilename();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType()); // MIME 타입 설정
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));
            return s3Client.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public void delete(Long imageId) {
        Image image = imageRepository.findByImageIdAndStatus(imageId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "이미지를 찾을 수 없습니다."));

        image.delete();
        deleteFile(image.getImageUrl());
    }

    public void deleteFile(String fileUrl) {
        // S3 URL에서 파일명 추출.
        String fileName = extractFileNameFromUrl(fileUrl);
        s3Client.deleteObject(bucketName, fileName);
    }

    private String extractFileNameFromUrl(String fileUrl) {
        // 파일 URL로부터 파일명을 추출하는 로직
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }

}

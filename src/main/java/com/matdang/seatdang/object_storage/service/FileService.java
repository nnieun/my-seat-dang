package com.matdang.seatdang.object_storage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.matdang.seatdang.object_storage.model.dto.FileDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);
//    private final AmazonS3Client amazonS3Client;
    private final AmazonS3 amazonS3Client;

    @Value("${spring.s3.bucket}")
    private String bucketName;

    @Value("${spring.s3.region}")
    private String region;

    // AWS S3 URL 생성
    private String generateS3Url(String keyName) {
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + keyName;
    }

    // UUID 파일명 생성
    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

    // 여러 개 파일 업로드
    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String filePath) {
        List<String> uploadFileUrls = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String originalFileName = multipartFile.getOriginalFilename();
            String uploadFileName = getUuidFileName(originalFileName);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            try (InputStream inputStream = multipartFile.getInputStream()) {
                String keyName = filePath + "/" + uploadFileName;

                // S3에 업로드
                amazonS3Client.putObject(
                        new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead));

                // 업로드된 파일 URL 생성
                String uploadFileUrl = generateS3Url(keyName);
                uploadFileUrls.add(uploadFileUrl);
            } catch (IOException e) {
                log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
                throw new RuntimeException("파일 업로드 실패", e);
            }
        }

        return uploadFileUrls;
    }

    // 하나의 파일만 업로드
    public String uploadSingleFile(MultipartFile multipartFile, String filePath) {
        String originalFileName = multipartFile.getOriginalFilename();
        String uploadFileName = getUuidFileName(originalFileName);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            String keyName = filePath + "/" + uploadFileName;

            // S3에 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            // 업로드된 파일 URL 생성
            return generateS3Url(keyName);
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    // 현재 업로드된 이미지 모두 가져오기
    public List<FileDto> listFiles(String filePath) {
        List<FileDto> files = new ArrayList<>();
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withPrefix(filePath + "/");
        ListObjectsV2Result result;

        do {
            result = amazonS3Client.listObjectsV2(req);

            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                String key = objectSummary.getKey();
                String fileName = key.substring(key.lastIndexOf("/") + 1);

                files.add(FileDto.builder()
                        .originalFileName(fileName)
                        .uploadFileName(fileName)
                        .uploadFilePath(filePath) // 현재 지정한 폴더만 가져옴
                        .uploadFileUrl(generateS3Url(key))
                        .build());
            }
            req.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());

        return files;
    }

    // AI 생성 이미지 업로드 (InputStream 사용)
    public String uploadFile(InputStream inputStream, String filePath, ObjectMetadata metadata) {
        String uploadFileName = getUuidFileName("generated-image");

        try {
            String keyName = filePath + "/" + uploadFileName;

            // S3에 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, keyName, inputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            // S3에 업로드된 파일의 URL 반환
            return generateS3Url(keyName);
        } catch (Exception e) {
            log.error("AI 생성 이미지 업로드 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }
}

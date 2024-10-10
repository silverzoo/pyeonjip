package com.team5.pyeonjip.product.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3BucketService {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Value("${aws.s3.bucket.url}")
    private String bucketUrl;

    /**
     * 단일 파일 업로드
     */
    public String upload(MultipartFile file, String path) throws IOException {
        String filename = generateFileName(file, path);
        ObjectMetadata metadata = createObjectMetadata(file);

        amazonS3.putObject(bucketName, filename, file.getInputStream(), metadata);
        amazonS3.setObjectAcl(bucketName, filename, CannedAccessControlList.PublicRead);

        return bucketUrl + "/" + filename;
    }

    /**
     * 다중 파일 업로드
     */
    public List<String> upload(MultipartFile[] files, String path) throws IOException {
        return Arrays.stream(files)
                .map(file -> {
                    try {
                        return upload(file, path);
                    } catch (IOException e) {
                        throw new RuntimeException("File upload failed: " + file.getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 파일 삭제 메서드
     */
    public boolean delete(String fileUrl) {
        String filename = fileUrl.replace(bucketUrl + "/", "");
        amazonS3.deleteObject(bucketName, filename);
        return true;
    }

    // Utility 메서드들

    private String generateFileName(MultipartFile file, String path) {
        String uuid = UUID.randomUUID().toString();
        return (path != null && !path.isEmpty() ? path + "/" : "") + uuid + "_" + file.getOriginalFilename();
    }

    private ObjectMetadata createObjectMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        return metadata;
    }
}
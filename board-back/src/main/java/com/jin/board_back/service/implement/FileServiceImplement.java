package com.jin.board_back.service.implement;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.jin.board_back.service.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImplement implements FileService{
    
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // @Value("${file.path}")
    // private String filePath;
    
    @Value("${file.url}")
    private String fileUrl;

    @Override
    public String upload(MultipartFile file) {

        if (file.isEmpty()) return null;

        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String saveFileName = uuid + extension;
        // String savePath = filePath + saveFileName;

        try{
            // file.transferTo(new File(savePath));
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3Client.putObject(bucket, saveFileName, file.getInputStream(), metadata);

        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        // String url = fileUrl + saveFileName;
        String url = amazonS3Client.getUrl(bucket, saveFileName).toString();
        
        return url;
    }

    @Override
    public Resource getImage(String fileName) {
        
        // Resource resource = null;

        try {
            // resource = new UrlResource("file:" + filePath + fileName);
            String url = amazonS3Client.getUrl(bucket, fileName).toString();
            return new UrlResource(url);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        // return resource;
    }
}

package com.karanSpringboot.s3FileStorage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class S3StorageService {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${application.bucket.name}$")
    private String bucket_name;

    public String uploadFileToBucket(MultipartFile multipartFile){
        File fileObj = convertMultiPartFileToFile(multipartFile);
        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        // convert multipartFile to File
        // putObject - String, File, or .. InputStream
        s3Client.putObject(new PutObjectRequest(bucket_name, fileName, fileObj));
        // delete the locally created file
        fileObj.delete();
        return "File uploaded success : " + fileName;
    }

    public byte[] downloadFile(String fileName){
        S3Object s3Object = s3Client.getObject(bucket_name, fileName);
        S3ObjectInputStream objectContent = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(objectContent);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deleteFile(String fileName){
        s3Client.deleteObject(bucket_name, fileName);
        return "File Deleted : "+ fileName;
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}

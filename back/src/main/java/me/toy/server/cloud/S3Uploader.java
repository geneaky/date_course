package me.toy.server.cloud;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.image.bucket}")
    private String bucket;

    public String upload(MultipartFile file, String saveFileName){
        String fileName = bucket + saveFileName;
        File uploadFile = convert(file);
        amazonS3.putObject(bucket,saveFileName,uploadFile);
        uploadFile.delete();
        return fileName;
    }

    public File convert(MultipartFile file){
        File convertFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertFile)){
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertFile;
    }
}

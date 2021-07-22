package me.toy.server.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomAwsConfiguration {

  @Value("${cloud.aws.credentials.access-key}")
  private String accessKey;

  @Value("${cloud.aws.credentials.secret-key}")
  private String secretKey;

  @Value("${cloud.aws.region.static}")
  private String region;

  @Bean
  public BasicAWSCredentials awsCredentialsProvider() {
    BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
    return basicAWSCredentials;
  }

  @Bean
  public AmazonS3 amazonS3() {
    AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentialsProvider()))
        .build();

    return amazonS3;
  }

}

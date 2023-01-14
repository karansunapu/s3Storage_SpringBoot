package com.karanSpringboot.s3FileStorage.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class s3ClientConfig {

    @Value("${cloud.aws.credentials.access-key}$")
    private String access_key;
    @Value("${cloud.aws.credentials.secret-key}$")
    private String secret_key;
    @Value("${cloud.aws.region.static}")
    private String region;


    @Bean
    public AmazonS3 s3Client(){
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2).build();
    }

}

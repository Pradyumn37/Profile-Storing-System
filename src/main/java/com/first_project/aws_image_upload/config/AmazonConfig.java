package com.first_project.aws_image_upload.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {

    @Bean
    public AmazonS3 s3(){
        AWSCredentials awsCredentials=new BasicAWSCredentials(
            "AKIA6F4R4WFQTLL3WEU7","jGBMYSWZ9Eu+uImH7bjM83tRskhXvLzKXUPcMItq"
        );
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.EU_NORTH_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

}

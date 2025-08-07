package com.first_project.aws_image_upload.fileStore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {
    private final AmazonS3 s3;

    @Autowired
    public FileStore(AmazonS3 s3) {
        this.s3 = s3;
    }

    public String save(String bucketName, String key, Optional<Map<String, String>> optionalMetadata, InputStream inputStream) {
        ObjectMetadata metadata = optionalMetadata.map(this::buildMetadata).orElseGet(ObjectMetadata::new);

        try {
            s3.putObject(bucketName, key, inputStream, metadata);
            return "https://" + bucketName + ".s3.amazonaws.com/" + key;
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to store file to S3: " + e.getErrorMessage(), e);
        }
    }

    private ObjectMetadata buildMetadata(Map<String, String> metadata) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        metadata.forEach(objectMetadata::addUserMetadata);
        if (metadata.containsKey("Content-Length")) {
            objectMetadata.setContentLength(Long.parseLong(metadata.get("Content-Length")));
        }
        if (metadata.containsKey("Content-Type")) {
            objectMetadata.setContentType(metadata.get("Content-Type"));
        }
        return objectMetadata;
    }

    public byte[] download(String path, String key) {
        try {
            S3Object object = s3.getObject(path, key);
            // Use IOUtils for a clean, one-line way to read the stream to a byte array
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (AmazonServiceException | IOException e) {
            // Catch IOException as well, since reading the stream can throw it
            throw new IllegalStateException("Failed to download file from S3", e);
        }
    }
}
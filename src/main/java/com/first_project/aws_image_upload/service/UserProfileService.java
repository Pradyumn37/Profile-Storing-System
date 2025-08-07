package com.first_project.aws_image_upload.service;

import com.first_project.aws_image_upload.bucket.BucketName;
import com.first_project.aws_image_upload.fileStore.FileStore;
import com.first_project.aws_image_upload.profile.UserProfile;
import com.first_project.aws_image_upload.profile.UserProfileDataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.springframework.util.MimeTypeUtils.*;

@Service
public class UserProfileService {

    private static final Set<String> ALLOWED_IMAGE_MIME_TYPES = Set.of(
            IMAGE_JPEG_VALUE,
            IMAGE_PNG_VALUE,
            IMAGE_GIF_VALUE
    );

    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    public List<UserProfile> getUserProfiles() {
        return userProfileDataAccessService.getUserProfiles();
    }

    public void uploadUserProfileId(UUID userProfileId, MultipartFile file) {
        // 1. Check if user exists
        UserProfile user = getUserProfileOrThrow(userProfileId);

        // 2. Validate the file
        validateFile(file);

        // 3. Grab metadata
        Map<String, String> metadata = extractMetadata(file);

        // 4. Construct the S3 bucket and the full key for the object
        String bucketName = BucketName.PROFILE_IMAGE.getBucketName();
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        // The full key includes the "folder" (user's ID) and the filename
        String key = String.format("%s/%s", user.getUserProfileId(), filename);

        try {
            // This assumes fileStore.save takes bucketName and the full key
            fileStore.save(bucketName, key, Optional.of(metadata), file.getInputStream());

            // 5. CRITICAL FIX: Save the leaf filename (the key) to the user's profile
            user.setUserProfileImageLink(filename);

        } catch (IOException e) {
            throw new IllegalStateException("Failed to store file to S3", e);
        }
    }

    public byte[] downloadUserProfileImage(UUID userProfileId) {
        // 1. Get the user profile
        UserProfile user = getUserProfileOrThrow(userProfileId);

        // 2. Get the stored filename (the leaf part of the key) from the user profile
        String filename = user.getUserProfileImageLink()
                .orElseThrow(() -> new IllegalStateException(String.format("User %s does not have a profile image", userProfileId)));

        // 3. Construct the S3 bucket and the full key, just like in the upload method
        String bucketName = BucketName.PROFILE_IMAGE.getBucketName();
        String key = String.format("%s/%s", user.getUserProfileId(), filename);

        // 4. Download from the file store using the correct bucket and full key
        return fileStore.download(bucketName, key);
    }

    private UserProfile getUserProfileOrThrow(UUID userProfileId) {
        // This is now highly efficient, fetching only one user
        return userProfileDataAccessService
                .findUserProfileById(userProfileId)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("User profile %s not found", userProfileId)));
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        if (!ALLOWED_IMAGE_MIME_TYPES.contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image [jpeg, png, gif]. Type found: " + file.getContentType());
        }
    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }
}
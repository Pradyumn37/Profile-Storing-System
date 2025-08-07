package com.first_project.aws_image_upload.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserProfileDataAccessService {

    private final FakeUserProfileDataStore fakeUserProfileDataStore;

    @Autowired
    public UserProfileDataAccessService(FakeUserProfileDataStore fakeUserProfileDataStore) {
        this.fakeUserProfileDataStore = fakeUserProfileDataStore;
    }

    public List<UserProfile> getUserProfiles(){
        // Correctly use the injected instance
        return fakeUserProfileDataStore.getUserProfiles();
    }

    // Add this new, efficient method to find a single user
    public Optional<UserProfile> findUserProfileById(UUID userProfileId) {
        return this.getUserProfiles().stream()
                .filter(profile -> profile.getUserProfileId().equals(userProfileId))
                .findFirst();
    }
}
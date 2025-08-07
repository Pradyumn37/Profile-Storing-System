package com.first_project.aws_image_upload.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@Repository
public class FakeUserProfileDataStore {

    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.fromString("7002d5ce-3bc9-4ef4-858c-085dc0d10999"), "JohnJones", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("afc38003-7e3b-4724-b57a-0f9a47b21ca4"), "Cunha", null));
    }

    public static List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }
}
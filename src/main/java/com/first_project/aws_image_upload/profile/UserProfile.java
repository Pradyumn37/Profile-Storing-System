package com.first_project.aws_image_upload.profile;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Optional;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    private UUID userProfileId;
    private String userName;
    private String userProfileImageLink; // this is the s3 key

//    public UserProfile(UUID userProfileId, String userName, String userProfileImageLink) {
//        this.userProfileId = userProfileId;
//        this.userName = userName;
//        this.userProfileImageLink = userProfileImageLink;
//    }
public Optional<String> getUserProfileImageLink() {
    return Optional.ofNullable(this.userProfileImageLink);
}


}

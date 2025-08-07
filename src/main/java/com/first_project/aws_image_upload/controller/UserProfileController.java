package com.first_project.aws_image_upload.controller;

import com.first_project.aws_image_upload.fileStore.FileStore;
import com.first_project.aws_image_upload.profile.UserProfile;
import com.first_project.aws_image_upload.service.UserProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/v1/user-profile")
@CrossOrigin("*")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping
    public List<UserProfile> getUserProfiles(){
        return userProfileService.getUserProfiles();
    }

    //@Pathvariable lets you take teh UUID from the URL and use it
    @PostMapping(
            path="{UserProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadUserProfileId(@PathVariable("UserProfileId")UUID userProfileId, @RequestParam("file")MultipartFile file) throws IOException {
        userProfileService.uploadUserProfileId(userProfileId,file);

    }

    @GetMapping(path="{UserProfileId}/image/download")
    public byte[] downloadUserProfileId(@PathVariable("UserProfileId")UUID userProfileId) throws IOException {
        return userProfileService.downloadUserProfileImage(userProfileId);
    }
}

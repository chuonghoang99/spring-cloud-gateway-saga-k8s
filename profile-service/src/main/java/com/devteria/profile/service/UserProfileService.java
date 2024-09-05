package com.devteria.profile.service;

import com.devteria.profile.dto.request.UserProfileRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.entity.UserProfile;
import com.devteria.profile.mapper.UserProfileMapper;
import com.devteria.profile.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {

    UserProfileRepository userProfileRepository;

    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(UserProfileRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile((request));
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUseProfileResponse(userProfile);
    }

    public UserProfileResponse getProfile(String id) {

        UserProfile userProfile =
                userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found !!!"));

        return userProfileMapper.toUseProfileResponse(userProfile);
    }

}

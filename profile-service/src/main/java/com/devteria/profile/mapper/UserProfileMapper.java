package com.devteria.profile.mapper;

import com.devteria.profile.dto.request.UserProfileRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(UserProfileRequest request);

    UserProfileResponse toUseProfileResponse(UserProfile response);

}

package com.devanshu.distributedlovable.accountservice.dto.auth;

import java.util.List;

import com.devanshu.distributedlovable.commonlib.dto.UserProfileDTO;

public record UsersListDTO(List<UserProfileDTO> userProfileDTOs) {

}

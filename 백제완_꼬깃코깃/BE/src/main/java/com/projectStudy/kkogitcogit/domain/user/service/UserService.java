package com.projectStudy.kkogitcogit.domain.user.service;

import com.projectStudy.kkogitcogit.domain.user.dto.request.UserJoinRequest;
import com.projectStudy.kkogitcogit.domain.user.dto.request.UserLoginRequest;
import com.projectStudy.kkogitcogit.domain.user.dto.response.UserJoinResponse;
import com.projectStudy.kkogitcogit.domain.user.dto.response.UserLoginResponse;

public interface UserService {

    UserJoinResponse join(UserJoinRequest userJoinRequest);

    UserLoginResponse login(UserLoginRequest userLoginRequest);
}

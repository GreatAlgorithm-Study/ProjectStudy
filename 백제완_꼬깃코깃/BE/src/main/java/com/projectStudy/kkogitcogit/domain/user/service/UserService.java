package com.projectStudy.kkogitcogit.domain.user.service;

import com.projectStudy.kkogitcogit.domain.user.dto.request.UserJoinRequest;
import com.projectStudy.kkogitcogit.domain.user.dto.response.UserJoinResponse;

public interface UserService {

    UserJoinResponse join(UserJoinRequest userJoinRequest);
}

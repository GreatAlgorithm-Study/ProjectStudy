package com.projectStudy.kkogitcogit.domain.member.service;

import com.projectStudy.kkogitcogit.domain.member.dto.request.UserJoinRequest;
import com.projectStudy.kkogitcogit.domain.member.dto.response.UserJoinResponse;

public interface UserService {

    UserJoinResponse join(UserJoinRequest userJoinRequest);
}

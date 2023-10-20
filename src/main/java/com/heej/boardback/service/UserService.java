package com.heej.boardback.service;

import org.springframework.http.ResponseEntity;

import com.heej.boardback.dto.request.user.PatchNicknameRequestDto;
import com.heej.boardback.dto.request.user.PatchProfileImageRequestDto;
import com.heej.boardback.dto.response.user.GetSignInUserResponseDto;
import com.heej.boardback.dto.response.user.GetUserResponseDto;
import com.heej.boardback.dto.response.user.PatchNicknameResponseDto;
import com.heej.boardback.dto.response.user.PatchProfileImageResponseDto;

public interface UserService {

    ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(String email);
    ResponseEntity<? super GetUserResponseDto> getUser(String email);

    ResponseEntity<? super PatchNicknameResponseDto> patchNickname(PatchNicknameRequestDto dto, String email);
    ResponseEntity<? super PatchProfileImageResponseDto> patchProfileImage(PatchProfileImageRequestDto dto, String email);

}

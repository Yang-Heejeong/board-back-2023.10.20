package com.heej.boardback.dto.response.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.heej.boardback.dto.response.ResponseCode;
import com.heej.boardback.dto.response.ResponseDto;
import com.heej.boardback.dto.response.ResponseMessage;
import com.heej.boardback.entity.UserEntity;

import lombok.Getter;

@Getter
public class GetSignInUserResponseDto extends ResponseDto {

    private String email;
    private String nickname;
    private String profileImage;

    private GetSignInUserResponseDto(String code, String message, UserEntity userEntity) {
        super(code, message);
        this.email = userEntity.getEmail();
        this.nickname = userEntity.getNickname();
        this.profileImage = userEntity.getProfileImageUrl();
    }

    public static ResponseEntity<GetSignInUserResponseDto> success(UserEntity userEntity) {
        GetSignInUserResponseDto result = new GetSignInUserResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, userEntity);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistUser() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXIST_USER, ResponseMessage.NOT_EXIST_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
    
}

package com.heej.boardback.dto.response.board;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.heej.boardback.dto.response.ResponseCode;
import com.heej.boardback.dto.response.ResponseDto;
import com.heej.boardback.dto.response.ResponseMessage;

import lombok.Getter;

@Getter
public class IncreaseViewCountResponseDto extends ResponseDto {
    
    private IncreaseViewCountResponseDto(String code, String message) {
        super(code, message);
    }

    public static ResponseEntity<IncreaseViewCountResponseDto> success() {
        IncreaseViewCountResponseDto result = new IncreaseViewCountResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistBoard() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXIST_BOARD, ResponseMessage.NOT_EXIST_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}

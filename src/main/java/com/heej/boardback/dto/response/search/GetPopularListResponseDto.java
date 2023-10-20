package com.heej.boardback.dto.response.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.heej.boardback.dto.response.ResponseCode;
import com.heej.boardback.dto.response.ResponseDto;
import com.heej.boardback.dto.response.ResponseMessage;
import com.heej.boardback.repository.resultSet.SearchWordResultSet;

import lombok.Getter;

@Getter
public class GetPopularListResponseDto extends ResponseDto {
    
    private List<String> popularWordList;

    private GetPopularListResponseDto(String code, String message, List<SearchWordResultSet> resultSets) {
        super(code, message);
        List<String> popularWordList  = new ArrayList<>();
        for(SearchWordResultSet resultSet: resultSets) {
            String word = resultSet.getSearchWord();
            popularWordList.add(word);
        }
        this.popularWordList = popularWordList;
    }

    // GetPopularListResponseDto를 사용해도 되고, ResponseDto로 사용해도 상관 없다. 확장 받아서 쓰는 거니까.
    public static ResponseEntity<ResponseDto> success(List<SearchWordResultSet> resultSets) {
        ResponseDto result = new GetPopularListResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, resultSets);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}

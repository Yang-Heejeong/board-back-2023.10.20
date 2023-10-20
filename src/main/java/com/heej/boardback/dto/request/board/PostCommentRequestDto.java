package com.heej.boardback.dto.request.board;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostCommentRequestDto {
    // 문자열에서만 사용!! //
    @NotBlank
    private String content;
}

package com.matdang.seatdang.common.exception_handler;

import com.matdang.seatdang.ai.dto.ErrorResponseDto;
import com.matdang.seatdang.common.exception.ImageLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ImageLimitExceededException.class)
    public ResponseEntity<ErrorResponseDto> handleImageLimitExceeded() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(
                        "생성 가능한 이미지 횟수가 부족합니다.",
                        LocalDateTime.now()
                ));
    }
}
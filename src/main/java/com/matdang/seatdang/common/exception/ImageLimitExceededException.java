package com.matdang.seatdang.common.exception;

public class ImageLimitExceededException extends RuntimeException{

    public ImageLimitExceededException(){
        super("생성 가능한 이미지 횟수가 부족합니다.");
    }
}

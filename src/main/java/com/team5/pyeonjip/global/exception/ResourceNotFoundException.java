//NOTE: 삭제 예정 코드. GlobalException 클래스를 사용해주세요.

package com.team5.pyeonjip.global.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
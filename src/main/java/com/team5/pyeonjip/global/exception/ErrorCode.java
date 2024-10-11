package com.team5.pyeonjip.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY-01", "카테고리를 찾을 수 없습니다."),
    INVALID_PARENT_SELF(HttpStatus.BAD_REQUEST, "CATEGORY-02", "자기 자신을 상위 카테고리로 설정할 수 없습니다."),
    INVALID_PARENT(HttpStatus.BAD_REQUEST, "CATEGORY-03", "존재하지 않는 카테고리를 상위 카테고리로 설정할 수 없습니다."),
    DUPLICATE_CATEGORY(HttpStatus.CONFLICT, "CATEGORY-04", "이미 존재하는 카테고리입니다."),
    CHANGE_TO_ROOT_CATEGORY(HttpStatus.ACCEPTED, "CATEGORY-05", "최상위 카테고리로 이동하시겠습니까?"),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT_ROOM-01", "채팅방을 찾을 수 없습니다."),
    CHAT_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT_MESSAGE-01", "메시지를 불러올 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT-01", "상품을 찾을 수 없습니다."),
    PRODUCT_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT-02", "상품 옵션을 찾을 수 없습니다."),
    PRODUCT_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT-03", "상품 이미지를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


}


//NOTE: 참고하실 수 있는 예시입니다.

/* 400 BAD_REQUEST : 잘못된 요청 */
/* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
//INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),
//
///* 404 NOT_FOUND : Resource를 찾을 수 없음 */
//USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 정보의 사용자를 찾을 수 없습니다."),
//
///* 409 : CONFLICT : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
//DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다."),
//
//HAS_EMAIL(HttpStatus.BAD_REQUEST, "ACCOUNT-002", "존재하는 이메일입니다."),
//INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "ACCOUNT-003", "비밀번호가 일치하지 않습니다."),
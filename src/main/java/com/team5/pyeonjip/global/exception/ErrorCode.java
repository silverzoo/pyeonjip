package com.team5.pyeonjip.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 카테고리
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY-01", "카테고리를 찾을 수 없습니다."),
    INVALID_PARENT_SELF(HttpStatus.BAD_REQUEST, "CATEGORY-02", "자기 자신을 상위 카테고리로 설정할 수 없습니다."),
    INVALID_PARENT(HttpStatus.BAD_REQUEST, "CATEGORY-03", "존재하지 않는 카테고리를 상위 카테고리로 설정할 수 없습니다."),
    DUPLICATE_CATEGORY(HttpStatus.CONFLICT, "CATEGORY-04", "이미 존재하는 카테고리입니다."),

    // 채팅
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT_ROOM-01", "채팅방을 찾을 수 없습니다."),
    CHAT_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT_MESSAGE-01", "메시지를 불러올 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT-01", "상품을 찾을 수 없습니다."),
    PRODUCT_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT-02", "상품 옵션을 찾을 수 없습니다."),
    PRODUCT_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT-03", "상품 이미지를 찾을 수 없습니다."),

    // 주문
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "STOCK-01", "재고 수량이 부족합니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER-01", "해당 주문을 찾을 수 없습니다."),
    USER_ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER-02", "해당 사용자의 주문을 찾을 수 없습니다."),
    DELIVERY_ALREADY_STARTED(HttpStatus.BAD_REQUEST, "DELIVERY-01", "배송이 시작된 주문은 취소할 수 없습니다."),

    // 유저
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-01", "사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-02", "이미 존재하는 이메일입니다."),
    INVALID_USER_UPDATE(HttpStatus.BAD_REQUEST, "USER-03", "유효하지 않은 수정 요청입니다."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-04", "계정을 찾을 수 없습니다."),
    USER_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "USER-05", "사용자 삭제에 실패했습니다."),
    USER_SIGNUP_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "USER-06", "회원가입에 실패했습니다."),
    // 유저 - 로그인
    INVALID_LOGIN_REQUEST(HttpStatus.BAD_REQUEST, "LOGIN-01", "잘못된 로그인 요청입니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "LOGIN-02", "인증에 실패했습니다."),
    LOGIN_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "LOGIN-03", "로그인 처리 중 오류가 발생했습니다."),
    // 유저 - JWT
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT-01", "Refresh 토큰을 찾을 수 없습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT-02", "Refresh 토큰이 만료되었습니다."),
    REFRESH_TOKEN_NOT_SAVED(HttpStatus.UNAUTHORIZED, "JWT-03", "Refresh 토큰이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "JWT-04", "유효하지 않은 Refresh 토큰입니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT-05", "Access 토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "JWT-06", "유효하지 않은 Access 토큰입니다."),
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "JWT-07", "토큰이 제공되지 않았습니다."),

    INVALID_LOGOUT_REQUEST(HttpStatus.BAD_REQUEST, "LOGOUT-01", "잘못된 로그아웃 요청입니다."),
    LOGOUT_MISSING_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "LOGOUT-02", "Refresh 토큰이 없습니다."),
    LOGOUT_INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "LOGOUT-03", "유효하지 않은 Refresh 토큰입니다."),
    LOGOUT_REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "LOGOUT-04", "Refresh 토큰이 만료되었습니다."),
    LOGOUT_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "LOGOUT-05", "로그아웃 처리 중 오류가 발생했습니다.");

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
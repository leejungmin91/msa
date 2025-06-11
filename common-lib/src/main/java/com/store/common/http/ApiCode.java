package com.store.common.http;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiCode {

	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, String.valueOf(HttpStatus.UNAUTHORIZED.value())),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, String.valueOf(HttpStatus.BAD_REQUEST.value())),
	NOT_FOUND(HttpStatus.NOT_FOUND, String.valueOf(HttpStatus.NOT_FOUND.value())),

	SUCCESS(HttpStatus.OK, "0000", "정상적으로 처리되었습니다."),
	FAIL(HttpStatus.OK,"1000", "요청이 실패했습니다."),
	DUPLICATE_MEMBER(HttpStatus.BAD_REQUEST, "1001", "이미 존재하는 회원입니다."),
	LOGIN_DENIED(HttpStatus.BAD_REQUEST, "1002", "아이디 또는 비밀번호를 확인해주세요."),
	ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "1003", "권한이 없습니다."),
	TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "1004", "토큰이 유효하지 않습니다."),
	TOKEN_EXPIRED(HttpStatus.INTERNAL_SERVER_ERROR, "1005", "토큰이 만료되었습니다."),
	NOT_FOUND_DATA(HttpStatus.BAD_REQUEST, "1006", "요청을 확인해주세요."),
	OUT_OF_STOCK(HttpStatus.INTERNAL_SERVER_ERROR, "1007", "상품 재고가 부족합니다."),
	FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "1008", "파일 저장에 실패하였습니다."),
	PENDING_MEMBER(HttpStatus.UNAUTHORIZED, "1009", "회원 승인 후 사용가능합니다. \n관리자에게 요청해주세요."),

	PAYMENT_FAIL(HttpStatus.BAD_REQUEST, "1010", "결제 실패되었습니다."),
	PAYMENT_MEMBER_DENIED(HttpStatus.BAD_REQUEST, "1011", "결제 요청한 회원정보가 불일치합니다."),
	PAYMENT_PAID_AMOUNT_DIFFERENT(HttpStatus.BAD_REQUEST, "1012", "지불금액이 주문금액과 다릅니다."),
	PAYMENT_CANCEL_NOT_COLLECT_NO(HttpStatus.BAD_REQUEST, "1013", "주문번호가 존재하지 않습니다."),
	PAYMENT_CANCEL_ALREADY(HttpStatus.BAD_REQUEST, "1014", "이미 취소한 주문입니다."),
	PAYMENT_CANCEL_FAIL(HttpStatus.BAD_REQUEST, "1015", "취소에 문제가 생겼습니다. 다시 시도해주시거나 관리자에게 문의해주세요."),

	;

	private HttpStatus status;
	private String code;
	private String message;

	ApiCode(HttpStatus status, String code){
		this.status = status;
		this.code = code;
	}

	ApiCode(HttpStatus status, String code, String message){
		this.status = status;
		this.code = code;
		this.message = message;
	}

}

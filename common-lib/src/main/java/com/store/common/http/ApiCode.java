package com.store.common.http;

import lombok.Getter;
import java.net.HttpURLConnection;

@Getter
public enum ApiCode {

	UNAUTHORIZED(HttpURLConnection.HTTP_UNAUTHORIZED),
	INTERNAL_SERVER_ERROR(HttpURLConnection.HTTP_INTERNAL_ERROR),
	BAD_REQUEST(HttpURLConnection.HTTP_BAD_REQUEST),
	NOT_FOUND(HttpURLConnection.HTTP_NOT_FOUND),

	SUCCESS(HttpURLConnection.HTTP_OK, "0000", "정상적으로 처리되었습니다."),
	FAIL(HttpURLConnection.HTTP_OK,"1000", "요청이 실패했습니다."),
	DUPLICATE_MEMBER(HttpURLConnection.HTTP_BAD_REQUEST, "1001", "이미 존재하는 회원입니다."),
	LOGIN_DENIED(HttpURLConnection.HTTP_BAD_REQUEST, "1002", "아이디 또는 비밀번호를 확인해주세요."),
	ACCESS_DENIED(HttpURLConnection.HTTP_UNAUTHORIZED, "1003", "권한이 없습니다."),
	TOKEN_NOT_VALID(HttpURLConnection.HTTP_UNAUTHORIZED, "1004", "토큰이 유효하지 않습니다."),
	TOKEN_EXPIRED(HttpURLConnection.HTTP_INTERNAL_ERROR, "1005", "토큰이 만료되었습니다."),
	NOT_FOUND_DATA(HttpURLConnection.HTTP_BAD_REQUEST, "1006", "요청을 확인해주세요."),
	OUT_OF_STOCK(HttpURLConnection.HTTP_INTERNAL_ERROR, "1007", "상품 재고가 부족합니다."),
	FILE_UPLOAD_FAIL(HttpURLConnection.HTTP_INTERNAL_ERROR, "1008", "파일 저장에 실패하였습니다."),
	PENDING_MEMBER(HttpURLConnection.HTTP_UNAUTHORIZED, "1009", "회원 승인 후 사용가능합니다. \n관리자에게 요청해주세요."),

	PAYMENT_FAIL(HttpURLConnection.HTTP_BAD_REQUEST, "1010", "결제 실패되었습니다."),
	PAYMENT_MEMBER_DENIED(HttpURLConnection.HTTP_BAD_REQUEST, "1011", "결제 요청한 회원정보가 불일치합니다."),
	PAYMENT_PAID_AMOUNT_DIFFERENT(HttpURLConnection.HTTP_BAD_REQUEST, "1012", "지불금액이 주문금액과 다릅니다."),
	PAYMENT_CANCEL_NOT_COLLECT_NO(HttpURLConnection.HTTP_BAD_REQUEST, "1013", "주문번호가 존재하지 않습니다."),
	PAYMENT_CANCEL_ALREADY(HttpURLConnection.HTTP_BAD_REQUEST, "1014", "이미 취소한 주문입니다."),
	PAYMENT_CANCEL_FAIL(HttpURLConnection.HTTP_BAD_REQUEST, "1015", "취소에 문제가 생겼습니다. 다시 시도해주시거나 관리자에게 문의해주세요."),

	;

	private int status;
	private String code;
	private String message;

	ApiCode(int status){
		this.status = status;
	}

	ApiCode(int status, String code){
		this.status = status;
		this.code = code;
	}

	ApiCode(int status, String code, String message){
		this.status = status;
		this.code = code;
		this.message = message;
	}

}

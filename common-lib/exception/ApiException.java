package com.store.common.exception;

import com.store.common.http.ApiCode;
import lombok.Getter;

import java.io.Serial;

@Getter
public class ApiException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 745917535698733911L;

	private final ApiCode apiCode;

	public ApiException(ApiCode apiCode) {
		super(apiCode.getMessage());
		this.apiCode = apiCode;
	}


}

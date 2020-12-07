package com.study.mk1.data;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class SecurityResult {
	
	private HttpStatus code;
	
	private int status;
	
	private String message;
	
	private boolean isSuccess;
	
	private String prevPage;

}

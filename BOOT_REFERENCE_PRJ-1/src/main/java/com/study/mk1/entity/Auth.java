package com.study.mk1.entity;

import com.study.mk1.abstracts.AbstractEntity;

import lombok.Data;

@Data
public class Auth extends AbstractEntity{
	
	private int authSeq;
	
	private String authCd;
	
	private String authNm;
	
}

package com.study.mk1.entity;

import com.study.mk1.abstracts.AbstractEntity;

import lombok.Data;

@Data
public class Mbr extends AbstractEntity {
	
	int mbrSeq;

	String mbrId;
	
	String mbrNm;
	
	String mbrPw;
	
	String mbrEmail;
	
	String mbrStatCd;
	
	String mbrTpCd;
	
	String mbrMobNationNo;
	
	String mbrMobAreaNo;
	
	String mbrMobTlofNo;
	
	String mbrMobTlofLstNo;
	
	String mbrGrdCd;
	
	String mobilNo;
	
}

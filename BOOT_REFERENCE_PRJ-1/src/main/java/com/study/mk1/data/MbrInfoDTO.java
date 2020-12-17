package com.study.mk1.data;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.entity.Auth;
import com.study.mk1.entity.Mbr;
import com.study.mk1.jpa.mbr.MbrJpa;

import lombok.Data;

@Data
public class MbrInfoDTO extends AbstractEntity{

	Mbr mbr;
	
	MbrJpa mbrJpa;
	
	Auth auth;
	
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
	
	private long mbrAuthMappingSeq;
	
}

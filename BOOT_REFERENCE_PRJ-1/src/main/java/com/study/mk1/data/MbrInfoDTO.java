package com.study.mk1.data;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.entity.Auth;
import com.study.mk1.entity.Mbr;

import lombok.Data;

@Data
public class MbrInfoDTO extends AbstractEntity{

	Mbr mbr;
	
	Auth auth;
	
	private String mbrAuthMappingSeq;
	
}

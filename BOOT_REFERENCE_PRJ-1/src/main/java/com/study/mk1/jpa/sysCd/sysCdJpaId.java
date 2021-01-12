package com.study.mk1.jpa.sysCd;

import javax.persistence.Column;
import javax.persistence.Id;

import com.study.mk1.abstracts.AbstractEntity;

import lombok.Data;

@Data
public class sysCdJpaId  extends AbstractEntity{

	String cd;
	
	String upperCd;
}

package com.study.mk1.jpa.mbrPetMapping;

import com.study.mk1.abstracts.AbstractEntity;

import lombok.Data;

@Data
public class MbrPetMappingId extends AbstractEntity{

	long mbrSeq;
	
	long petSeq;
	
}

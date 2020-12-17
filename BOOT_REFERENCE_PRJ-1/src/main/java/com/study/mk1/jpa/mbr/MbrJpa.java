package com.study.mk1.jpa.mbr;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.study.mk1.abstracts.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Entity(name="Mbr")
@AllArgsConstructor
@Data
public class MbrJpa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	@Builder
	public MbrJpa(String mbrId, String mbrNm, String mbrPw, String mbrEmail, String mbrStatCd, String mbrTpCd, String mbrMobNationNo, String mbrMobAreaNo,
				String mbrMobTlofNo, String mbrMobTlofLstNo, String mbrGrdCd ) {
		
		this.mbrId = mbrId;
		
		this.mbrNm = mbrNm;
		
		this.mbrPw = mbrPw;
		
		this.mbrEmail = mbrEmail;
		
		this.mbrStatCd = mbrStatCd;
		
		this.mbrTpCd = mbrTpCd;
		
		this.mbrMobNationNo = mbrMobNationNo;
		
		this.mbrMobAreaNo = mbrMobAreaNo;
		
		this.mbrMobTlofNo = mbrMobTlofNo;

		this.mbrMobTlofLstNo = mbrMobTlofLstNo;

		this.mbrGrdCd = mbrGrdCd;
		
	}

	
}

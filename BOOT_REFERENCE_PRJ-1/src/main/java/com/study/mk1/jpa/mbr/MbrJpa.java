package com.study.mk1.jpa.mbr;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "mbr")
@AllArgsConstructor
public class  MbrJpa extends AbstractEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mbr_seq", updatable = false, insertable = false)
	long mbrSeq;
	
	@Column(name = "mbr_id")
	String mbrId;
	
	@Column(name = "mbr_nm")
	String mbrNm;

	@Column(name = "mbr_pw")
	String mbrPw;

	@Column(name = "mbr_email")
	String mbrEmail;

	@Column(name = "mbr_stat_cd")
	String mbrStatCd;

	@Column(name = "mbr_tp_cd")
	String mbrTpCd;

	@Column(name = "mbr_mob_nation_no")
	String mbrMobNationNo;

	@Column(name = "mbr_mob_area_no")
	String mbrMobAreaNo;

	@Column(name = "mbr_mob_tlof_no")
	String mbrMobTlofNo;

	@Column(name = "mbr_mob_tlof_lst_no")
	String mbrMobTlofLstNo;

	@Column(name = "mbr_grd_cd")
	String mbrGrdCd;
	
	
	
	@OneToOne(fetch = FetchType.LAZY,mappedBy = "mbrJpa")
	MbrAuthMappingJpa mam;
	
	 MbrJpa() {
		
	}

	public long getMbrSeq() {
		return mbrSeq;
	}

	public void setMbrSeq(long mbrSeq) {
		this.mbrSeq = mbrSeq;
	}

	public String getMbrId() {
		return mbrId;
	}

	public void setMbrId(String mbrId) {
		this.mbrId = mbrId;
	}

	public String getMbrNm() {
		return mbrNm;
	}

	public void setMbrNm(String mbrNm) {
		this.mbrNm = mbrNm;
	}

	public String getMbrPw() {
		return mbrPw;
	}

	public void setMbrPw(String mbrPw) {
		this.mbrPw = mbrPw;
	}

	public String getMbrEmail() {
		return mbrEmail;
	}

	public void setMbrEmail(String mbrEmail) {
		this.mbrEmail = mbrEmail;
	}

	public String getMbrStatCd() {
		return mbrStatCd;
	}

	public void setMbrStatCd(String mbrStatCd) {
		this.mbrStatCd = mbrStatCd;
	}

	public String getMbrTpCd() {
		return mbrTpCd;
	}

	public void setMbrTpCd(String mbrTpCd) {
		this.mbrTpCd = mbrTpCd;
	}

	public String getMbrMobNationNo() {
		return mbrMobNationNo;
	}

	public void setMbrMobNationNo(String mbrMobNationNo) {
		this.mbrMobNationNo = mbrMobNationNo;
	}

	public String getMbrMobAreaNo() {
		return mbrMobAreaNo;
	}

	public void setMbrMobAreaNo(String mbrMobAreaNo) {
		this.mbrMobAreaNo = mbrMobAreaNo;
	}

	public String getMbrMobTlofNo() {
		return mbrMobTlofNo;
	}

	public void setMbrMobTlofNo(String mbrMobTlofNo) {
		this.mbrMobTlofNo = mbrMobTlofNo;
	}

	public String getMbrMobTlofLstNo() {
		return mbrMobTlofLstNo;
	}

	public void setMbrMobTlofLstNo(String mbrMobTlofLstNo) {
		this.mbrMobTlofLstNo = mbrMobTlofLstNo;
	}

	public String getMbrGrdCd() {
		return mbrGrdCd;
	}

	public void setMbrGrdCd(String mbrGrdCd) {
		this.mbrGrdCd = mbrGrdCd;
	}

	public MbrAuthMappingJpa getMam() {
		return mam;
	}

	public void setMam(MbrAuthMappingJpa mam) {
		this.mam = mam;
	}
	

	
}

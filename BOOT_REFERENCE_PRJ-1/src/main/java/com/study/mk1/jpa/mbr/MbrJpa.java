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
@NoArgsConstructor
@Data
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
	
}

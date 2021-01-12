package com.study.mk1.jpa.mbr;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpa;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpa;
import com.study.mk1.jpa.mbrPrjMapping.MbrPrjMappingJpa;

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
	MbrAuthMappingJpa mbrAuthMappingJpa;
	
	/* 프로젝트 모집 서비스용 (사용안함)
	 * @OneToMany(fetch = FetchType.LAZY,mappedBy = "mbrJpa")
	List<MbrPrjMappingJpa> mpm = new ArrayList<MbrPrjMappingJpa>();*/
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "mbrJpa")
	List<MbrPetMappingJpa> mbrPetMappingJpa = new ArrayList<MbrPetMappingJpa>();
	
}

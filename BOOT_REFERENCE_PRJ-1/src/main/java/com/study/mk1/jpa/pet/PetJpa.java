package com.study.mk1.jpa.pet;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpa;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpa;
import com.study.mk1.jpa.mbrPrjMapping.MbrPrjMappingJpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pet")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PetJpa extends AbstractEntity{
	
	/**
	 * 반려 가족
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pet_seq", updatable = false, insertable = false)
	long petSeq;
	
	@Column(name = "pet_nm")
	String petNm;
	
	@Column(name = "pet_char_cd")
	String petCharCd;
	
	@Column(name = "pet_stat_cd")
	String petStatCd;
	
	@Column(name = "pet_grtng_wd")
	String petGrtngWd;
	
	@Column(name = "pet_spec")
	String petSpec;
	
	@Column(name = "pet_intro")
	String petIntro;
	
	@Column(name = "pet_birth")
	Date petBirth;
	
	@OneToOne(fetch = FetchType.LAZY,mappedBy = "petJpa")
	MbrPetMappingJpa mbrPetMappingJpa;
	

}

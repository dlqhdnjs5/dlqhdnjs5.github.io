package com.study.mk1.jpa.sysCd;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpa;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingId;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpa;
import com.study.mk1.jpa.sysUpperCd.SysUpperCdJpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sys_cd")
@AllArgsConstructor
@NoArgsConstructor
@Data
@IdClass(SysCdJpa.class)
public class SysCdJpa extends AbstractEntity{
	
	@Id
	@Column(name = "cd", updatable = false, insertable = false)
	String cd;
	
	@Id
	@Column(name = "upper_cd", updatable = false, insertable = false)
	String upperCd;
	
	@Column(name = "cd_nm", updatable = false, insertable = false)
	String cdNm;

	/*@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "upper_cd" , updatable = false, insertable = false)
	SysUpperCdJpa sysUpperCdJpa;*/
}

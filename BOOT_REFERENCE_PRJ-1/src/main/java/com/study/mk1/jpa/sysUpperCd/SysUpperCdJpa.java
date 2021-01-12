package com.study.mk1.jpa.sysUpperCd;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpa;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingId;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpa;
import com.study.mk1.jpa.sysCd.SysCdJpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sys_upper_cd")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUpperCdJpa extends AbstractEntity{
	
	@Id
	@Column(name = "upper_cd", updatable = false, insertable = false)
	private String upperCd;
	
	@Column(name = "upper_cd_nm", updatable = false, insertable = false)
	private String upperCdNm;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name="upper_cd")
	private List<SysCdJpa> sysCdJpa = new ArrayList<SysCdJpa>();
	
	

}

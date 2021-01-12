package com.study.mk1.jpa.mbrPrjMapping;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.auth.AuthJpa;
import com.study.mk1.jpa.coronaInfo.CoronaInfoJpa;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpa;
import com.study.mk1.jpa.prj.PrjJpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mbr_prj_mapping")
@AllArgsConstructor
@NoArgsConstructor
@Data
@IdClass(MbrPrjMappingJpa.class)
public class MbrPrjMappingJpa  extends AbstractEntity{
	
	@Id
	@Column(name = "mbr_seq", updatable = false, insertable = true)
	long mbrSeq;
	
	@Id
	@Column(name = "prj_seq", updatable = false, insertable = true)
	long prjSeq;
	
	@Column(name = "prj_role_cd")
	String prjRoleCd;
	
	
	@Column(name = "join_dt")
	Date joinDt;
	
	@Column(name = "end_dt")
	Date endDt;
	
	@Column(name = "join_stat_cd")
	String joinStatCd;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "mbr_seq" , updatable = false, insertable = false)
	MbrJpa mbrJpa;
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name = "prj_seq" , updatable = false, insertable = false)
	PrjJpa prjJpa;
	
}

package com.study.mk1.jpa.prj;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbrPrjMapping.MbrPrjMappingJpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prj")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrjJpa extends AbstractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prj_seq", updatable = false, insertable = false)
	long prjSeq;
	
	@Column(name = "prj_stat_cd")
	String prjStatCd;
	
	@Column(name = "prj_nm")
	String prjNm;

	@Column(name = "prj_cont")
	String prjCont;

	@Column(name = "prj_applcn_cnt")
	String prjApplcnCnt;

	@Column(name = "prj_beg_dt")
	Date prjBegDt;

	@Column(name = "prj_end_dt")
	Date prjEndDt;

	@Column(name = "prj_rprst_img_url")
	String prjRprstImgUrl;

	@Column(name = "prj_rprst_img_nm")
	String prjRprstImgNm;
	
	@Column(name = "prj_intro")
	String prjIntro;

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "prjJpa")
	List<MbrPrjMappingJpa> mpm = new ArrayList<MbrPrjMappingJpa>();
}

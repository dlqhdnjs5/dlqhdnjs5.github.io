package com.study.mk1.jpa.coronaInfo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.study.mk1.abstracts.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "corona_info")
@NoArgsConstructor
@AllArgsConstructor
@Data
@IdClass(CoronaInfoJpa.class)
public class CoronaInfoJpa extends AbstractEntity {
	
	@Id
	@Column(name = "create_dt")
	private String createdDt;
	
	@Id
	@Column(name = "city_nm")
	private String cityNm;
	
	@Column(name = "def_cnt")
	private String defCnt;
	
	@Column(name = "death_cnt")
	private String deathCnt;
	
	@Column(name = "isolatiing_cnt")
	private String isolatingCnt;
	
	@Column(name = "isol_clear_cnt")
	private String isolClearCnt;
	
	@Column(name = "inc_dec")
	private String incDec;
	
	

}

package com.study.mk1.jpa.auth;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.entity.Auth;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth")
@AllArgsConstructor
public class AuthJpa extends AbstractEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auth_seq", updatable = false, insertable = false)
	private long authSeq;
	
	@Column(name = "auth_cd")
	private String authCd;
	
	@Column(name = "auth_nm")
	private String authNm;
	
	public AuthJpa() {
		
	}
	
	
	
	public long getAuthSeq() {
		return authSeq;
	}



	public void setAuthSeq(long authSeq) {
		this.authSeq = authSeq;
	}



	public String getAuthCd() {
		return authCd;
	}



	public void setAuthCd(String authCd) {
		this.authCd = authCd;
	}



	public String getAuthNm() {
		return authNm;
	}



	public void setAuthNm(String authNm) {
		this.authNm = authNm;
	}



	public List<MbrAuthMappingJpa> getMbrAuthMappingJpa() {
		return mbrAuthMappingJpa;
	}



	public void setMbrAuthMappingJpa(List<MbrAuthMappingJpa> mbrAuthMappingJpa) {
		this.mbrAuthMappingJpa = mbrAuthMappingJpa;
	}



	@OneToMany(mappedBy = "authJpa" )
	List<MbrAuthMappingJpa> mbrAuthMappingJpa = new ArrayList<MbrAuthMappingJpa>();
	
	
}

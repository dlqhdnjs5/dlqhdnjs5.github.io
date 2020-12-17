package com.study.mk1.jpa.mbrAuthMapping;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.entity.Auth;
import com.study.mk1.entity.Mbr;
import com.study.mk1.jpa.auth.AuthJpa;
import com.study.mk1.jpa.mbr.MbrJpa;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "mbr_auth_mapping")
@AllArgsConstructor
public class MbrAuthMappingJpa extends AbstractEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mbr_auth_seq", updatable = false, insertable = false)
	private long mbrAuthSeq;
	
	@Column(name = "mbr_seq")
	private long mbrSeq;
	
	@Column(name = "auth_seq")
	private long authSeq;
	
	
	
	public MbrAuthMappingJpa() {
		
	}
	
	public long getMbrAuthSeq() {
		return mbrAuthSeq;
	}

	public void setMbrAuthSeq(long mbrAuthSeq) {
		this.mbrAuthSeq = mbrAuthSeq;
	}

	public long getMbrSeq() {
		return mbrSeq;
	}

	public void setMbrSeq(long mbrSeq) {
		this.mbrSeq = mbrSeq;
	}

	public long getAuthSeq() {
		return authSeq;
	}

	public void setAuthSeq(long authSeq) {
		this.authSeq = authSeq;
	}

	public MbrJpa getMbrJpa() {
		return mbrJpa;
	}

	public void setMbrJpa(MbrJpa mbrJpa) {
		this.mbrJpa = mbrJpa;
	}

	public AuthJpa getAuthJpa() {
		return authJpa;
	}

	public void setAuthJpa(AuthJpa authJpa) {
		this.authJpa = authJpa;
	}

	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "mbr_seq" , updatable = false, insertable = false)
	MbrJpa mbrJpa;
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name = "auth_seq" , updatable = false, insertable = false)
	AuthJpa authJpa;
	
}

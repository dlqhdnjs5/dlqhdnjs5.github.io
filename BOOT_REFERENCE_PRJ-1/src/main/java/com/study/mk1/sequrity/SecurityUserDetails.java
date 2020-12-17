package com.study.mk1.sequrity;

import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.study.mk1.entity.Auth;
import com.study.mk1.entity.Mbr;
import com.study.mk1.jpa.auth.AuthJpa;
import com.study.mk1.jpa.mbr.MbrJpa;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityUserDetails implements UserDetails {

	
	/**
	 * mybatis 사용
	 */
	Mbr mbr;
	Auth authField;
	
	/**
	 * jpa 사용
	 */
	MbrJpa mbrJpa;
	AuthJpa authJpa;
    
	private String type;
	
	 /* 권한 파라미터 리스트*/
    List<GrantedAuthority> grantedAuths;
    
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
		/*jpa 와 mybatis 구분*/
		if(mbr != null && authField != null) {
			auth.add(new SimpleGrantedAuthority(authField.getAuthCd()));
		}else if(mbrJpa != null && authJpa != null) {
			auth.add(new SimpleGrantedAuthority(authJpa.getAuthCd()));
		}
		
		this.setType();
		
		return auth;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return type.equals(approchType.MYBATIS.toString()) ?  mbr.getMbrPw() : mbrJpa.getMbrPw();
		//return mbr.getMbrPw();
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return type.equals(approchType.MYBATIS.toString()) ?  mbr.getMbrId() : mbrJpa.getMbrId();
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	/*mybatis*/
	public SecurityUserDetails(Mbr mbr,Auth authParam) {
		this.mbr = mbr;
		this.authField = authParam;
		getAuthorities();
	}
	
	/*jpa*/
	public SecurityUserDetails(MbrJpa mbr,AuthJpa authParam) {
		this.mbrJpa = mbr;
		this.authJpa = authParam;
		getAuthorities();
	}
	
	public  void setType() {
		if(mbr != null && authField != null) {
			this.type = approchType.MYBATIS.toString();
		}else if(mbrJpa != null && authJpa != null) {
			this.type = approchType.JPA.toString();
		}
	}
	
	private enum approchType {
		MYBATIS , JPA;
	}


}

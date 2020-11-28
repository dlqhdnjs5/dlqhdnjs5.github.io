package com.study.mk1.sequrity;

import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.study.mk1.entity.Auth;
import com.study.mk1.entity.Mbr;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityUserDetails implements UserDetails {

	Mbr mbr;
	
	Auth authField;
    
	 /* 권한 파라미터 리스트*/
    List<GrantedAuthority> grantedAuths;
    
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
		auth.add(new SimpleGrantedAuthority(authField.getAuthCd()));
		return auth;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return mbr.getMbrPw();
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return mbr.getMbrId();
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
	public SecurityUserDetails(Mbr mbr,Auth authParam) {
		this.mbr = mbr;
		this.authField = authParam;
		getAuthorities();
	}


}

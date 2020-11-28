package com.study.mk1.sequrity;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationProvider  {

	
//	@Override
//	public Authentication authenticate (Authentication authentication) throws AuthenticationException {
//		
//		String userId = authentication.getName();
//        String password = (String) authentication.getCredentials();
//        
//        //TODO : VO 만들고 userservice 로직 만들어서 나머지 처리
//	}
	
}

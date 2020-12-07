package com.study.mk1.common;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.context.SecurityContext;
import com.study.mk1.controllers.DefaultController;

public class IOService {
	
	private static Logger log = LoggerFactory.getLogger(IOService.class);
	
	/* 롤을 가지고 있는지 체크*/
	@SuppressWarnings("unchecked")
	public static boolean hasRole() {
		boolean hasRole = false;
		try {
			if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
					&& SecurityContextHolder.getContext().getAuthentication().getAuthorities() != null) {
				Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext()
						.getAuthentication().getAuthorities();

				for (GrantedAuthority authority : authorities) {
					hasRole = authority.getAuthority().equals("ROLE_USER");
					if (hasRole) {
						break;
					}
				}
			}
		} catch (Exception e) {
			log.info("=======================================");
			log.info("hasRole() error occour :: " + e.getMessage());
			log.info("=======================================");
		}
		return hasRole;
	}
	
	/* 스프링 시큐리티로 로그인된 사용자 세션정보를 얻는다. */
	public static Object getCurrentUserDetail() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null || context.getAuthentication() == null) {
			return null;
		} else {
			return context.getAuthentication().getPrincipal();
		}
	}

}

package com.study.mk1.common;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.study.mk1.controllers.SimpleTestController;

public class IOService {
	
	private static Logger log = LoggerFactory.getLogger(SimpleTestController.class);
	
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

}

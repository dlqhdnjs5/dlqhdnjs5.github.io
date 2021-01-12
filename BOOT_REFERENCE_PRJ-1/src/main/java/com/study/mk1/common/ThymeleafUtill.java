package com.study.mk1.common;

import org.springframework.stereotype.Component;

@Component("thymeleafUtil")
public class ThymeleafUtill {
	
	public String getLibsUrl(String path) {
		if ("/libs".endsWith("/") && path.startsWith("/")) {
			path = path.substring(1);
		}
		return "/libs"+path;
	}
	
	public String getStaticUrl(String path) {
		return "/static"+path;
	}
}

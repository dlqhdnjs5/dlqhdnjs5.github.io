package com.study.mk1.common;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
	  

	    @Override
	    public void sessionCreated(HttpSessionEvent se) {
	        se.getSession().setMaxInactiveInterval(60*60); //세션 1시간
	    }



	    @Override
	    public void sessionDestroyed(HttpSessionEvent se) {

	    }
}

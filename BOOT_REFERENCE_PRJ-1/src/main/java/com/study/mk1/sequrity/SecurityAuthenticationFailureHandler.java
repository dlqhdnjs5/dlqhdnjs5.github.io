package com.study.mk1.sequrity;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.mk1.data.SecurityResult;

@Component
public class SecurityAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		
		ObjectMapper mapper = new ObjectMapper();	//JSON 변경용

		SecurityResult securityResult = new SecurityResult();
		securityResult.setCode(HttpStatus.UNAUTHORIZED);
		securityResult.setStatus(response.SC_UNAUTHORIZED);
		securityResult.setMessage("아이디 혹은 비밀번호가 일치하지 않습니다.");
		securityResult.setSuccess(false);
		
    	response.setCharacterEncoding("UTF-8");
    	response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(mapper.writeValueAsString(securityResult));
        response.getWriter().flush();
        
//      PrintWriter pw = response.getWriter();
//      pw.write("error accured");
	
	}
}

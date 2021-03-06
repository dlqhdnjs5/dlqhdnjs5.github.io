package com.study.mk1.controllers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.GrantedAuthority;
import com.study.mk1.cmp.repositorys.MbrRepository;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.entity.Mbr;
import com.study.mk1.interceptors.CustomHandlerImpl;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.sequrity.SecurityUserDetailService;

@Controller
public class DefaultController {
	
	private static Logger log = LoggerFactory.getLogger(DefaultController.class);
	
	@Autowired
	MbrRepository mbrRepository;
	
	@Autowired
	SecurityUserDetailService securityUserDetailService;
	
	@RequestMapping(value= {"/home","/"})
	public String home(HttpServletRequest rquest , HttpServletResponse response)  {
		
		log.info("home");
		//TODO : 로그인 만든후 회원가입 만들기
		return "display/index";
		
	}
	
	@RequestMapping(value= {"/login"})
	public String login(HttpServletRequest rquest , HttpServletResponse response ,
			@RequestParam(value= "error",required=false) String error)  {
		
		if(error != null) {
			return "display/loginFail";
		}else {
			return "display/login";
			
		}
		
	}
	
	@RequestMapping(value= {"/logout"})
	public String logout(HttpServletRequest request , HttpServletResponse response) throws Exception {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if(auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		
		return "redirect:/login";
		
	}
	
	@RequestMapping(value= {"/signUpPage"})
	public String joinPage(HttpServletRequest request , HttpServletResponse response) throws Exception {
		
		return "display/signUpPage";
		
	}
	
	@RequestMapping(value="/join")
	public String joinMbr(HttpServletRequest request , HttpServletResponse response, MbrInfoDTO mbrInfoDto) throws Exception {
		
		try {
			
			if(mbrInfoDto != null && mbrInfoDto.getMbrJpa() != null) {
				
				String[] mobilNo = mbrInfoDto.getMobilNo().split("-");
				mbrInfoDto.getMbrJpa().setMbrMobAreaNo(mobilNo[0]);
				mbrInfoDto.getMbrJpa().setMbrMobTlofNo(mobilNo[1]);
				mbrInfoDto.getMbrJpa().setMbrMobTlofLstNo(mobilNo[2]);
				
				securityUserDetailService.joinMbr(mbrInfoDto);
				
			}else {
				NullPointerException e = new NullPointerException();
				throw e;
			}
			
		}catch(NullPointerException e) {
			log.warn("NullPointerException :  {}",mbrInfoDto);
		}catch(Exception e) {
			throw new Exception();
		}
		
		return "redirect:/login";
	}
		

}

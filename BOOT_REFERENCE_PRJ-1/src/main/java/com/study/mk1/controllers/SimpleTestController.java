package com.study.mk1.controllers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.mk1.cmp.repositorys.MbrRepository;
import com.study.mk1.entity.Mbr;
import com.study.mk1.interceptors.CustomHandlerImpl;

@Controller
public class SimpleTestController {
	
	private static Logger log = LoggerFactory.getLogger(SimpleTestController.class);
	
	@Autowired
	MbrRepository mbrRepository;
	
	@RequestMapping(value="/helloWolrd")
	public void helloWorld(HttpServletRequest rquest , HttpServletResponse response)  {
		
		
		try(
			Writer writer = response.getWriter();
			PrintWriter	wp =response.getWriter();
			)
		{
			
			writer.write("helloWolrd!");
			wp.write("helloWolrd2!");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	@RequestMapping(value= {"/home","/"})
	public String home(HttpServletRequest rquest , HttpServletResponse response)  {
		
		log.info("home");
		
		Mbr mbr = mbrRepository.selectMbr("brandon01");
		
		log.info("result :  {} ", mbr);
		
		//TODO : 로그인 만든후 회원가입 만들기
		return "display/index";
		
	}

}

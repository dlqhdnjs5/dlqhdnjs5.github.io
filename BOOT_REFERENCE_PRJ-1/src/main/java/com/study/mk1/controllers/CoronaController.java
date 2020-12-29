package com.study.mk1.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.mk1.jpa.coronaInfo.CoronaInfoJpa;
import com.study.mk1.jpa.coronaInfo.CoronaInfoJpaRepository;

@Controller
@RequestMapping("/corona")
public class CoronaController {
	
	@Autowired
	CoronaInfoJpaRepository coronaInfoJpaRepository;
	
	@GetMapping("/getCoronaInfo")
	@ResponseBody
	public Page<CoronaInfoJpa> getCoronaInfo(HttpServletRequest request,Pageable pageable) {
		
		Page<CoronaInfoJpa> result =  coronaInfoJpaRepository.findAllExceptTotal(pageable);
		
		return result;
	}

}

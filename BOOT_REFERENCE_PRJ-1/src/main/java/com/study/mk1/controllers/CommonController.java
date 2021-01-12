package com.study.mk1.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.mk1.jpa.sysCd.SysCdJpa;
import com.study.mk1.jpa.sysUpperCd.SysUpperCdJpa;
import com.study.mk1.jpa.sysUpperCd.SysUpperCdJpaRepository;

@RestController
@RequestMapping("/common")
public class CommonController {

	@Autowired
	SysUpperCdJpaRepository sysUpperCdJpaRepository;
	
	/**
	 * upperCd 로 cd목록 조회
	 * @param req
	 * @param res
	 * @param upperCd
	 * @return
	 */
	@GetMapping("/getCdbyUpperCd/{upperCd}")
	public SysUpperCdJpa getCdByUpperCd(HttpServletRequest req , HttpServletResponse res,
			@PathVariable(value="upperCd") String upperCd) {
		SysUpperCdJpa sysUpperCd =sysUpperCdJpaRepository.findByUpperCd(upperCd);
		
		return sysUpperCd;
	}
}

package com.study.mk1.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.mk1.cmp.components.MbrComponent;

@Controller
@RequestMapping("/member/api")
public class MemberController {
	
	
	@Autowired
	MbrComponent mbrComponent;
	
	@GetMapping(value="/checkMbrId")
	@ResponseBody
	public boolean checkMbrId(HttpServletRequest request,@RequestParam(value="mbrId") String mbrId) {
		
		return mbrComponent.checkMbrId(mbrId);
	}
	
}

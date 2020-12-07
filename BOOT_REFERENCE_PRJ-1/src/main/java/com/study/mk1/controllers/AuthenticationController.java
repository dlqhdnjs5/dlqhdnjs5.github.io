package com.study.mk1.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.study.mk1.common.IOService;

@Controller
public class AuthenticationController {
	
	@GetMapping(value = "/authenticated")
    public ResponseEntity<Void> authenticated() {
        if (IOService.hasRole()) {
            return ResponseEntity.ok().build();
        } 
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}

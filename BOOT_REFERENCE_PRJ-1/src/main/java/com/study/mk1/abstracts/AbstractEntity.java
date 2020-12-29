package com.study.mk1.abstracts;

import lombok.Data;

import java.io.Serializable;
import java.lang.*;

@Data
public class AbstractEntity implements Serializable {
	
	String regterId;
	
	String regDt; 
	
	String udterId;
	
	String udtDt;
}

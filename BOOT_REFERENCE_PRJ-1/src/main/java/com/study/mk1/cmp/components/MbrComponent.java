package com.study.mk1.cmp.components;

import org.springframework.stereotype.Component;

import com.study.mk1.data.MbrInfoDTO;

public interface MbrComponent {
	
	/**
	 * 회원가입 Mybatis , JPA 분기
	 * @param mbrInfoDTO
	 * @throws Exception
	 */
	public void joinMbr( MbrInfoDTO mbrInfoDTO ) throws Exception;
	
}

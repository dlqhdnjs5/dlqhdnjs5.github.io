package com.study.mk1.cmp.components;

import org.springframework.stereotype.Component;

import com.study.mk1.data.MbrInfoDTO;

public interface MbrComponent {
	
	/**
	 * 회원가입
	 * @param mbrInfoDTO
	 * @throws Exception
	 */
	public void joinMbr( MbrInfoDTO mbrInfoDTO ) throws Exception;
}

package com.study.mk1.cmp.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.study.mk1.cmp.services.MbrService;
import com.study.mk1.data.MbrInfoDTO;

@Transactional
@Component
public class MbrComponetImpl implements MbrComponent {
	
	@Autowired
	MbrService mbrService;
	
	/**
	 * 회원가입
	 */
	public void joinMbr(MbrInfoDTO mbrInfoDTO) throws Exception {
		
		mbrService.joinMbr(mbrInfoDTO);
		
	}
	

}

package com.study.mk1.cmp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.mk1.cmp.repositorys.AuthRepository;
import com.study.mk1.cmp.repositorys.MbrRepository;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.sequrity.SecurityUserDetailService;

@Service
public class MbrService {
	
	private static Logger log = LoggerFactory.getLogger(MbrService.class);
	
	@Autowired
	MbrRepository mbrRepository;
	
	@Autowired
	AuthRepository authRepository;
	
	/**
	 * 회원가입
	 */
	public void joinMbr(MbrInfoDTO mbrInfoDTO) {
		
		try {
			
			mbrRepository.insertMbr(mbrInfoDTO.getMbr());
			
			authRepository.insertMbrAuthMappingUseSelectKey(mbrInfoDTO);
			
		}catch(Exception e) {
			e.printStackTrace();
			log.error(this.getClass().getName() +"joinMbr() ERROR --> param : {}",mbrInfoDTO);
			throw new RuntimeException();
		}
		
		
	}

}

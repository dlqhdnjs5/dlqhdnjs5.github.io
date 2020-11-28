package com.study.mk1.cmp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.mk1.cmp.repositorys.AuthRepository;
import com.study.mk1.cmp.repositorys.MbrRepository;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.entity.Mbr;
import com.study.mk1.sequrity.SecurityUserDetails;

@Service
public class AuthService {
	
	@Autowired
	private MbrRepository mbrRepository;
	
	@Autowired
	private AuthRepository authRepository;
	
	public SecurityUserDetails getSecurityUserDetails(String userId) {
		MbrInfoDTO mbrInfo = mbrRepository.selectMbrInfo(userId);
		SecurityUserDetails userDetail = new SecurityUserDetails(mbrInfo.getMbr(),mbrInfo.getAuth());
		
		return userDetail;
	}
	
}

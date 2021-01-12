package com.study.mk1.sequrity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.study.BootReferencePrj1Application;
import com.study.mk1.cmp.components.MbrComponent;
import com.study.mk1.cmp.repositorys.MbrRepository;
import com.study.mk1.cmp.services.AuthService;
import com.study.mk1.data.MbrAuthEnum;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.entity.Auth;
import com.study.mk1.entity.Mbr;
import com.study.mk1.jpa.mbr.MbrJpa;

@Component
public class SecurityUserDetailService implements UserDetailsService {
	
	private static long ROLE_USER = 1;
	private static long ROLE_ADMIN = 4;
	
	private static Logger log = LoggerFactory.getLogger(SecurityUserDetailService.class);
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private MbrComponent mbrComponent;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		UserDetails userDetail = authService.getSecurityUserDetails(username);
		
		if(userDetail.getUsername() == null) {
			throw new UsernameNotFoundException("username " + username + " not found");
		}
		
		log.info("***************User exist***************");
		
		return userDetail;
	}
	
	public void joinMbr(Mbr mbr ) throws Exception {
		
		log.debug(this.getClass().getName() + ".mbrJoin() --> param : {}",mbr);
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		mbr.setMbrPw(passwordEncoder.encode(mbr.getMbrPw()));
		mbr.setMbrGrdCd(MbrAuthEnum.mbrGrdCd.GNRL.toString());
		mbr.setMbrStatCd(MbrAuthEnum.mbrStatCd.ACT.toString());
		mbr.setMbrTpCd(MbrAuthEnum.mbrTpCd.GNRL.toString());
		
		MbrInfoDTO mbrInfoDTO = new MbrInfoDTO();
		mbrInfoDTO.setMbr(mbr);
		mbrInfoDTO.setMbrAuthMappingSeq(ROLE_USER);
		
		mbrComponent.joinMbr(mbrInfoDTO);
		
	}
	
	public void joinMbr(MbrInfoDTO mbrInfoDto ) throws Exception {
		
		log.debug(this.getClass().getName() + ".mbrJoin() --> param : {}",mbrInfoDto.getMbrJpa());
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		mbrInfoDto.getMbrJpa().setMbrPw(passwordEncoder.encode(mbrInfoDto.getMbrJpa().getMbrPw()));
		mbrInfoDto.getMbrJpa().setMbrGrdCd(MbrAuthEnum.mbrGrdCd.GNRL.toString());
		mbrInfoDto.getMbrJpa().setMbrStatCd(MbrAuthEnum.mbrStatCd.ACT.toString());
		mbrInfoDto.getMbrJpa().setMbrTpCd(MbrAuthEnum.mbrTpCd.GNRL.toString());
		mbrInfoDto.setMbrAuthMappingSeq(ROLE_USER);
		
		mbrComponent.joinMbr(mbrInfoDto);
		
	}


}

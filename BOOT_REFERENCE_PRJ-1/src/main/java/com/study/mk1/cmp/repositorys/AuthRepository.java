package com.study.mk1.cmp.repositorys;

import org.springframework.stereotype.Repository;

import com.study.mk1.abstracts.AbstractRepository;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.entity.Mbr;

@Repository
public class AuthRepository extends AbstractRepository {

	public void insertMbrAuthMappingUseSelectKey(MbrInfoDTO mbrInfoDTO) {
		getSession1().insert("com.study.mk1.datasource1.auth.insertMbrAuthMappingUseSelectKey",mbrInfoDTO);
	}

		
}

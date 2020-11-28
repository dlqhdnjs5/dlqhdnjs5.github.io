package com.study.mk1.cmp.repositorys;

import org.springframework.stereotype.Repository;

import com.study.mk1.abstracts.AbstractRepository;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.entity.Mbr;

@Repository
public class MbrRepository extends AbstractRepository {

	public Mbr selectMbr(String mbrId) {
		return getSession1().selectOne("com.study.mk1.datasource1.mbr.selectMbrTable",mbrId);
	}
	
	public void insertMbr(Mbr mbr) {
		 getSession1().insert("com.study.mk1.datasource1.mbr.insertMbr",mbr);
	}
	
	public MbrInfoDTO selectMbrInfo(String mbrId) {
		return getSession1().selectOne("com.study.mk1.datasource1.security.selectMbrInfo",mbrId);
	}
		
}

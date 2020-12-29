package com.study.mk1.cmp.components;

import java.util.List;

import com.study.mk1.jpa.coronaInfo.CoronaInfoJpa;

public interface CoronaCommonComponent {
	
	public void insertCoronaInfo(List<CoronaInfoJpa> coronaInfoLists);

}

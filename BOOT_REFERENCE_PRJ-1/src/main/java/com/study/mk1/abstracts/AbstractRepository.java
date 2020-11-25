package com.study.mk1.abstracts;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractRepository {

		@Autowired
		private SqlSession sqlSession;
		
		protected SqlSession getSession1() {
			//return this.sqlSession1;
			return this.sqlSession;
		}
}

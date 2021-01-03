package com.study.mk1.common;

import java.sql.Connection;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.study.mk1.interfaces.CoronaVirusApiController;

@Component
public class SqlRunner implements ApplicationRunner{
	
	private static Logger log = LoggerFactory.getLogger(SqlRunner.class);
	
	private DataSource dataSource;
	
	public SqlRunner(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		
		try(Connection connection = dataSource.getConnection()){
			log.info(dataSource.getClass().toString());
			log.info(connection.getMetaData().getURL());
			log.info(connection.getMetaData().getUserName());
		}
		
	}
	
	

}

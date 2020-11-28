package com.study;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@Configuration
@ComponentScan(basePackages = "com.study")
public class BootReferencePrj1Application {
	
	private static Logger log = LoggerFactory.getLogger(BootReferencePrj1Application.class);

	public static void main(String[] args) {
		SpringApplication.run(BootReferencePrj1Application.class, args);
		log.info("Starter application Started!");
	}
	
	
	/**
	 * 세션팩토리 설정
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		
		Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath:META-INF/mybatis/datasource1/*/*.xml");
		sessionFactory.setMapperLocations(res);
		
		return sessionFactory.getObject();
		
	}

}

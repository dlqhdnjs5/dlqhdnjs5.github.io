package com.study.mk1.config;

import javax.servlet.http.HttpSessionListener;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.study.mk1.common.SessionListener;
import com.study.mk1.interceptors.CustomHandlerImpl;

@Configuration
public class CustomConfig implements WebMvcConfigurer{

	/*정적자원*/
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/static/", "classpath:/public/", "classpath:/",
            "classpath:/resources/", "classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/" };
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(new CustomHandlerImpl());
	}
	
	/*@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/", ".jsp");
	}*/
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
	
	@Bean
	public HttpSessionListener httpSessionListener(){
	    return new SessionListener();
	}
	
	/*스케쥴러 스레드 풀*/
	@Bean public TaskScheduler poolScheduler() { 
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler(); 
		threadPoolTaskScheduler.setPoolSize(Runtime.getRuntime().availableProcessors() * 5); 
		threadPoolTaskScheduler.setThreadNamePrefix("schaduler thread pool"); 
		return threadPoolTaskScheduler; 
	}
	

}

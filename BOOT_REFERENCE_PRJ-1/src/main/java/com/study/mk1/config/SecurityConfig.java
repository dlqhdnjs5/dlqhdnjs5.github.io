package com.study.mk1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String LOGIN_PAGE_URL = "/login";
	private static final String LOGIN_PROCESSING_URL = "/loginProcesse";
	private static final String LOGIN_FAIL = "/login?error";
	private static final String SUCCESS_URL = "/";
	private static final String USER_NAME_PRAMETER = "email";
	private static final String PASS_PARAMETER = "password";
	private static final String LOGOUT_URL = "/logout";
	
	
	
//	@Autowired
//	private UserAuthenticationProvider authenticationProvider;
	  
	/*정적 자원에 대해서는 Security 설정을 적용하지 않는다*/	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
	@Override
	protected void configure(HttpSecurity http ) throws Exception {
		
		http.authorizeRequests()
			.antMatchers("/css/**", "/js/**", "/img/**","/","/main","/helloWolrd","home").permitAll()
			.antMatchers("/auth/admin/**").hasAnyRole("ADMIN")
			.antMatchers("/auth/**").hasAnyRole("USER")
			.anyRequest().authenticated();
		
		http.formLogin()
			.loginPage(LOGIN_PAGE_URL)
			.loginProcessingUrl(LOGIN_PROCESSING_URL)
			.failureUrl(LOGIN_FAIL)
			.defaultSuccessUrl(SUCCESS_URL)
			.usernameParameter(USER_NAME_PRAMETER)
			.passwordParameter(PASS_PARAMETER)
			.permitAll();
		
		http.logout()
			.logoutUrl(LOGOUT_URL)
			.logoutSuccessUrl(LOGIN_PAGE_URL)
			.permitAll();
		
		http.csrf().disable();//?
		
	}
	
//	@Override
//    protected void configure(AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(authenticationProvider);
//    }
}

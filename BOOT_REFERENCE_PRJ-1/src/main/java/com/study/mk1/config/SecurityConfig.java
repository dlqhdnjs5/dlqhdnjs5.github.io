package com.study.mk1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.study.mk1.sequrity.SecurityAuthenticationFailureHandler;
import com.study.mk1.sequrity.SecurityAuthenticationSuccessHandler;
import com.study.mk1.sequrity.SecurityUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String LOGIN_PAGE_URL = "/login";
	private static final String LOGIN_SUCCESS_URL = "/loginSuccess";
	private static final String LOGIN_PROCESSING_URL = "/loginProcesse";
	private static final String LOGIN_FAIL = "/login?error";
	private static final String SUCCESS_URL = "/home";
	private static final String USER_NAME_PRAMETER = "email";
	private static final String PASS_PARAMETER = "password";
	private static final String LOGOUT_URL = "/logout";
	private static final String ROLE = "ROLE_";
	
	
	
//	@Autowired
//	private UserAuthenticationProvider authenticationProvider;
	
	@Autowired
	private SecurityUserDetailService securityUserDetailService;
	
	@Autowired
	private SecurityAuthenticationSuccessHandler securityAuthenticationSuccessHandler;
	
	@Autowired
	private SecurityAuthenticationFailureHandler securityAuthenticationFailureHandler;
	  
	/*정적 자원에 대해서는 Security 설정을 적용하지 않는다*/	
	
	/*서비스 전체에 영향을 미치는 설정으로 사용됨. 
	예컨대 리소스를 무시하여 엔드포인트에서 해당 리소스에 대해 CSRF를 방지하는 등의 보안절차를 건너뛰게 하거나, 
	디버그 모드를 세팅하거나, 방화벽 설정을 커스텀하여 특정 리퀘스트를 거부할 수 있다 
	위 예제에서도 web.ignoring 메소드를 통해 css, js, fonts, templates 파일 안의 리소스들에 대한 
	보안절차를 생략하고 있다..*/
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
	/* HttpSecurity 클래스를 주입함으로써 특정 http 요청에 대한 웹 기반 보안을 구성할 수 있도록 도와주는 메소드. 
	 일반적인 http 취약성에 대한 방어가 필요한 모든 엔드포인트를 여기에 지정할 수 있음.
	 특히 HttpSecurity에서 지원하는 authorizeRequest 메소드는 URL 패턴을 통한 
	 HttpServletRequest 접근에 대한 제한권한을 획득.*/
	@Override
	protected void configure(HttpSecurity http ) throws Exception {
	
		http.authorizeRequests()
			.antMatchers("/css/**", "/js/**", "/img/**").permitAll()
			.antMatchers(LOGIN_PAGE_URL.toString()).permitAll()
			.antMatchers("/auth/admin/**").hasRole("ADMIN")
			.antMatchers("/auth/**","/","/home","/ui/**").hasAnyRole("USER","ADMIN")
			.antMatchers("/**").permitAll()
			.anyRequest().permitAll();

//		
		http.formLogin()
			.loginPage(LOGIN_PAGE_URL.toString())
			.successHandler(securityAuthenticationSuccessHandler)
			.failureHandler(securityAuthenticationFailureHandler)
			.loginProcessingUrl(LOGIN_PROCESSING_URL.toString())
			//.failureUrl(LOGIN_FAIL)
			//.defaultSuccessUrl(SUCCESS_URL)
			.usernameParameter(USER_NAME_PRAMETER)
			.passwordParameter(PASS_PARAMETER)
			.permitAll();
		
		


		
		http.logout()
			.logoutUrl(LOGOUT_URL)
			.logoutSuccessUrl(LOGIN_PAGE_URL)
			.invalidateHttpSession(true) /*로그아웃시 세션 제거*/
			.deleteCookies("JSESSIONID")
			.clearAuthentication(true) /*권한정보 제거*/
			.permitAll()
		.and()
			.sessionManagement()
			.maximumSessions(1)
			.maxSessionsPreventsLogin(false)
			.expiredUrl("/")
			.sessionRegistry(sessionRegistry());
		
		
		http.csrf();
		
	}
	 /*AuthenticationManager()를 획득하기 위해 AuthenticationManager()의 기본 구현에 사용. 
	 오버로드 된 경우 AuthenticationManagerBuilder를 사용하여 AuthenticationManager를 지정.
	 AuthenticationManagerBuilder의 userDetailsService() 메소드는 인자의 사용자 클래스가
	 UserDetailService 인터페이스를 구현하는 경우 해당 클래스를 DaoAuthenticationConfigurer 클래스의 인자에 담아
	 DaoAuthenticationConfigurer 객체를 리턴시킴으로써 사용자 클래스를 DaoAuthenticationConfigurer 클래스의 인자로써 인가절차를 커스텀.
	 예제에서 비밀번호 암호화 메소드로 사용되는 passwordEncoder()는 AbstractDaoAuthenticationConfigurer클래스의 메소드*/
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.authenticationProvider(authenticationProvider);
		auth.userDetailsService(securityUserDetailService).passwordEncoder(passwordEncoder());
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	 @Bean
	    public SessionRegistry sessionRegistry() {
	        return new SessionRegistryImpl();
	    }





}

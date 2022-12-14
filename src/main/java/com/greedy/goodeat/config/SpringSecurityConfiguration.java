package com.greedy.goodeat.config;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.greedy.goodeat.user.member.service.AuthenticationService;

@EnableWebSecurity
public class SpringSecurityConfiguration {
	
	private final AuthenticationService authenticationService;
	
	public SpringSecurityConfiguration(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean WebSecurityCustomizer configure() {
		return (web) -> web.ignoring().antMatchers("/css/**", "js/**", "/images/**");
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		Map<String, List<String>> permitListMap = authenticationService.getPermitListMap();
		
		List<String> adminPermitList = permitListMap.get("admin");
		List<String> memberPermitList = permitListMap.get("member");
		
		return http
			.csrf()
				.disable()
			.authorizeHttpRequests()
				.antMatchers(memberPermitList.toArray(new String [memberPermitList.size()])).hasAnyRole("MEMBER", "ADMIN")
				.antMatchers(adminPermitList.toArray(new String[adminPermitList.size()])).hasRole("ADMIN")
				.anyRequest().permitAll()
			.and()
				.formLogin()
				.loginPage("/login")
				.successHandler(successHandler())
				.failureForwardUrl("/loginfail")
			.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
				.deleteCookies("JSESSIONID")
				.invalidateHttpSession(true)
				.logoutSuccessUrl("/")
			.and()
				.rememberMe()
				.key("goodeatRememberMe")
				.rememberMeCookieName("remember-me")
				.tokenValiditySeconds(60 * 60 *24 * 7)
				.userDetailsService(authenticationService)
			.and()
				.oauth2Login()
				.loginPage("/login")
				.defaultSuccessUrl("/")
			.and()
				.build();
	}
	
	private AuthenticationSuccessHandler successHandler() {
		return new CustomLoginSuccessHandler();
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		
		return http
				.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(authenticationService)
				.passwordEncoder(passwordEncoder())
				.and()
				.build();
	}
	
}

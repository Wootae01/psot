package com.example.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import com.example.board.oauth2.CustomOauth2Service;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOauth2Service customOauth2Service;



	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

		//csrf 는 사용자가 인증된 세션을 이용해서 공격자가 의도한 요청을 보내도록 함.
		//jwt 는 stateless 하기 때문에 꺼도 됨.
		http
			.csrf((auth) -> auth.disable());

		http
			.formLogin(auth -> auth.disable());

		http
			.httpBasic((auth) -> auth.disable());

		//oauth2 설정
		http
			.oauth2Login((oauth2) -> oauth2
				.userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
					.userService(customOauth2Service))));

		http
			.logout(logout -> logout
				.logoutUrl("/logout")             // (기본값) 로그아웃 처리 URL
				.logoutSuccessUrl("/")            // 로그아웃 후 리다이렉트할 URL
				.invalidateHttpSession(true)  //세션 무효화
				.deleteCookies("JSESSIONID") //브라우저에 저장된 JSESSIONID 삭제
				.clearAuthentication(true) //SecurityContext 에 저장된 인증 객체 제거
			);

		//인가 설정
		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/post", "/oauth2/**", "/login/**", "/").permitAll()
				.requestMatchers(new RegexRequestMatcher("/post/[0-9]+", null)).permitAll()
				.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

				.anyRequest().authenticated());


		return http.build();
	}
}

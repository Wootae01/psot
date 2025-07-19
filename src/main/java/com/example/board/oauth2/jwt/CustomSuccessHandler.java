package com.example.board.oauth2.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.board.oauth2.CustomOauth2User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JWTUtil jwtUtil;
	private final RefreshRepository refreshRepository;


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		//Principal 은 인증 완료 후 로그인한 사용자를 나타내는 객체
		CustomOauth2User customUserDetail =  (CustomOauth2User) authentication.getPrincipal();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String username = customUserDetail.getName();
		String role = auth.getAuthority();
		log.info("로그인 성공, {}", username);
		String access = jwtUtil.createJwt(JWTConsts.ACCESS, username, role, JWTConsts.ACCESS_TOKEN_EXPIRATION);
		String refresh = jwtUtil.createJwt(JWTConsts.REFRESH, username, role, JWTConsts.REFRESH_TOKEN_EXPIRATION);
		RefreshEntity refreshEntity = new RefreshEntity();

		response.addCookie(createCookie("Authorization", access));
		response.sendRedirect("http://localhost:8080/post");
	}

	private Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(60 * 60 * 60);
		cookie.setPath("/");
		cookie.setHttpOnly(true);

		return cookie;
	}
}

package com.example.board.oauth2.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.board.dto.UserDTO;
import com.example.board.oauth2.CustomOauth2User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	public JWTFilter(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	//OncePerRequestFilter : 하나의 HTTP 요청(request)에 대해 한 번만 필터가 실행되도록 보장해 주는 것
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String token = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("Authorization")) {
				token = cookie.getValue();
			}
		}

		if (token == null) {
			log.info("jwt token null");
			filterChain.doFilter(request, response);
			return;
		}

		if (jwtUtil.isExpired(token)) {
			log.info("jwt is expired");
			filterChain.doFilter(request, response);
			return;
		}

		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);

		UserDTO userDTO = new UserDTO();
		userDTO.setRole(role);
		userDTO.setUsername(username);

		CustomOauth2User customOauth2User = new CustomOauth2User(userDTO);
		Authentication authToken = new UsernamePasswordAuthenticationToken(
			customOauth2User, null, customOauth2User.getAuthorities());

		//SecurityContext에 Authentication(인증 정보) 저장
		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}
}

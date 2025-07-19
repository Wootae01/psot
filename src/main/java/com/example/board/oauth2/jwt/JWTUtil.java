package com.example.board.oauth2.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {

	//jwt key 서명하고 검증하는데 사용할 객체
	private SecretKey secretKey;


	public JWTUtil(@Value("${spring.jwt.secret}") String secrete) {
		secretKey = new SecretKeySpec(secrete.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS512.key().build().getAlgorithm());
	}

	public String getCategory(String token) {
		return Jwts.parser().verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("category", String.class);
	}

	public String getUsername(String token) {

		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload().get("username", String.class);
	}

	public String getRole(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("role", String.class);
	}

	public Boolean isExpired(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getExpiration()
			.before(new Date());
	}

	public String createJwt(String category, String username, String role, Long expiredMs) {
		return Jwts.builder()
			.claim("category", category)
			.claim("username", username)
			.claim("role", role)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiredMs))
			.signWith(secretKey)
			.compact();
	}
}

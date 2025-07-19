package com.example.board.oauth2.jwt;

public class JWTConsts {
	public static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 10; // 10분
	public static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 14; // 2주
	public static final String REFRESH = "refresh";
	public static final String ACCESS = "access";
}

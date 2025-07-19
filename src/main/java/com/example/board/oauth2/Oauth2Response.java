package com.example.board.oauth2;

public interface Oauth2Response {

	//naver, google, kakao 등등
	String getProvider();

	//제공자에서 발급해주는 아이디
	String getProviderId();

	String getEmail();

	String getName();
}

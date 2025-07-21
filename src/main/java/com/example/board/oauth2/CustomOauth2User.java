package com.example.board.oauth2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.board.dto.UserDTO;

public class CustomOauth2User implements OAuth2User {

	private final UserDTO userDTO;

	public CustomOauth2User(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	@Override
	public Map<String, Object> getAttributes() {
		Map<String, Object> attrs = new HashMap<>();
		attrs.put("username", userDTO.getUsername());
		attrs.put("nickname", userDTO.getNickname());
		attrs.put("role", userDTO.getRole());
		return attrs;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return userDTO.getRole();
			}
		});

		return collection;
	}

	@Override
	public String getName() {
		return userDTO.getName();
	}

	public String getUsername() {
		return userDTO.getUsername();
	}


	public String getNickname() {
		return userDTO.getNickname();
	}
}

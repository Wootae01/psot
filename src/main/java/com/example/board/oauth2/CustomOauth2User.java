package com.example.board.oauth2;

import java.util.ArrayList;
import java.util.Collection;
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
		return null;
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
		//principle 식별자는 username 으로 설정
		return userDTO.getUsername();
	}

	public String getNickname() {
		return userDTO.getNickname();
	}
}

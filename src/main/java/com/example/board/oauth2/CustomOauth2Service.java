package com.example.board.oauth2;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.board.domain.User;
import com.example.board.dto.UserDTO;
import com.example.board.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomOauth2Service extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	public CustomOauth2Service(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		log.info("oauth2 user = ", oAuth2User);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		Oauth2Response oAuth2Response = null;
		if (registrationId.equals("google")) {
			oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
		} else{
			return null;
		}

		String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
		User data = userRepository.findByUsername(username);
		if (data == null) {
			User user = new User();
			user.setUsername(username);
			user.setName(oAuth2Response.getName());
			user.setRole("ROLE_USER");
			user.setEmail(oAuth2Response.getEmail());
			user.setNickname("ㅇㅇ");
			userRepository.save(user);

			UserDTO userDTO = new UserDTO();
			userDTO.setUsername(username);
			userDTO.setName(oAuth2Response.getName());
			userDTO.setRole("ROLE_USER");
			userDTO.setNickname("ㅇㅇ");
			return new CustomOauth2User(userDTO);

		} else{
			UserDTO userDTO = new UserDTO();
			userDTO.setUsername(data.getUsername());
			userDTO.setName(oAuth2Response.getName());
			userDTO.setRole(data.getRole());
			userDTO.setNickname(data.getNickname());
			return new CustomOauth2User(userDTO);
		}

	}
}

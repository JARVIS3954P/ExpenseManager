package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.entity.User;
import com.zidioDev.ExpenseManager.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public OAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        
        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = extractEmail(attributes, provider);
        
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return oauth2User;
        }

        User newUser = createUser(attributes, provider);
        userRepository.save(newUser);
        
        return oauth2User;
    }

    private String extractEmail(Map<String, Object> attributes, String provider) {
        if ("google".equals(provider)) {
            return (String) attributes.get("email");
        } else if ("github".equals(provider)) {
            return (String) attributes.get("email");
        }
        throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
    }

    private User createUser(Map<String, Object> attributes, String provider) {
        User user = new User();
        if ("google".equals(provider)) {
            user.setEmail((String) attributes.get("email"));
            user.setName((String) attributes.get("name"));
        } else if ("github".equals(provider)) {
            user.setEmail((String) attributes.get("email"));
            user.setName((String) attributes.get("name"));
        }
        user.setRole("ROLE_USER");
        user.setProvider(provider);
        return user;
    }
} 
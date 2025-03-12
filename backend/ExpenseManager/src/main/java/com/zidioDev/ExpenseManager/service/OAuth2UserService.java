package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.model.User;
import com.zidioDev.ExpenseManager.model.enums.AuthProvider;
import com.zidioDev.ExpenseManager.model.enums.Role;
import com.zidioDev.ExpenseManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String providerName = userRequest.getClientRegistration().getRegistrationId();
        AuthProvider provider = AuthProvider.valueOf(providerName.toUpperCase());
        
        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = extractEmail(attributes, provider);
        
        userRepository.findByEmail(email)
            .orElseGet(() -> userRepository.save(createUser(attributes, provider)));
        
        return oauth2User;
    }

    private String extractEmail(Map<String, Object> attributes, AuthProvider provider) {
        String email = switch (provider) {
            case GOOGLE, GITHUB -> (String) attributes.get("email");
            default -> throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
        };
        
        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }
        return email;
    }

    private User createUser(Map<String, Object> attributes, AuthProvider provider) {
        String name = switch (provider) {
            case GOOGLE -> (String) attributes.get("name");
            case GITHUB -> (String) attributes.get("login");
            default -> throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
        };

        return User.builder()
                .fullName(name)
                .email(extractEmail(attributes, provider))
                .provider(provider)
                .role(Role.EMPLOYEE)
                .build();
    }
} 


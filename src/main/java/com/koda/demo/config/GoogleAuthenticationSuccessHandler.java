package com.koda.demo.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koda.demo.GoogleUser;
import com.koda.demo.domain.User;
import com.koda.demo.domain.UserRepository;
import com.koda.demo.domain.UserRoleRepository;

@Component
public class GoogleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private HttpSession httpSession;
	private ObjectMapper objectMapper;
	private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
	
	public GoogleAuthenticationSuccessHandler(HttpSession httpSession, ObjectMapper objectMapper, UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.httpSession = httpSession;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        httpSession.setAttribute(SessionConstants.LOGIN_USER, getUser(getGoogleUser(authentication)));
        httpSession.setMaxInactiveInterval(60*30);
        response.sendRedirect("/me");
    }
	
	private GoogleUser getGoogleUser(Authentication authentication) { 
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        return objectMapper.convertValue(oAuth2Authentication.getUserAuthentication().getDetails(), GoogleUser.class);
    }
	
	private User getUser(GoogleUser google){
        User savedUser = userRepository.findByEmail(google.getEmail());

        if(savedUser == null){
            User newUser = google.toEntity();
            newUser.addRole(userRoleRepository.findDefaultRole());
            savedUser = userRepository.save(newUser);
        }

        return savedUser;
    }
}

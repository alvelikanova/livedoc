package com.livedoc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.livedoc.bl.domain.entities.User;
import com.livedoc.bl.services.UserService;

@Component
public class SecurityUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findUserByLoginName(username);
		if (user != null) {
			return new SecurityUserDetails(user);
		}
		throw new UsernameNotFoundException(String.format("User with username %s was not found", username));
	}

}

package com.livedoc.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.livedoc.bl.domain.entities.User;

public class SecurityUserDetails implements UserDetails {

	private static final long serialVersionUID = -7014403758030285546L;
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	private User user;

	public SecurityUserDetails(User user) {
		super();
		this.user = user;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		String role = user.getRole().getCode();
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
		return Arrays.asList(authority);
	}

	public String getPassword() {
		return user.getPassword();
	}

	public String getUsername() {
		return user.getName();
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isUserAdmin() {
		for (GrantedAuthority authority : getAuthorities()) {
			if (authority.getAuthority().equals(ROLE_ADMIN)) {
				return true;
			}
		}
		return false;
	}

	public User getUser() {
		return user;
	}

}

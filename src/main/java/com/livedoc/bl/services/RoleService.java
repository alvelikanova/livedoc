package com.livedoc.bl.services;

import java.util.List;

import com.livedoc.bl.domain.entities.Role;

public interface RoleService {
	
	List<Role> findAllRoles();
}

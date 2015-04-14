package com.livedoc.bl.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.bl.domain.entities.Role;
import com.livedoc.bl.services.RoleService;
import com.livedoc.dal.entities.RoleEntity;
import com.livedoc.dal.providers.RoleDataProvider;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDataProvider roleDataProvider;
	@Autowired
	private DozerBeanMapper mapper;

	@Transactional
	public List<Role> findAllRoles() {
		List<Role> roles = new ArrayList<Role>();
		List<RoleEntity> roleEntities = roleDataProvider.findAll();
		for (RoleEntity roleEntity : roleEntities) {
			Role role = mapper.map(roleEntity, Role.class);
			roles.add(role);
		}
		return roles;
	}

}

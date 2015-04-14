package com.livedoc.dal.providers.impl;

import org.springframework.stereotype.Repository;

import com.livedoc.dal.entities.RoleEntity;
import com.livedoc.dal.providers.RoleDataProvider;

@Repository
public class RoleDataProviderImpl extends BaseDataProvider<RoleEntity, String>
		implements RoleDataProvider {

	public RoleDataProviderImpl() {
		super(RoleEntity.class);
	}

}

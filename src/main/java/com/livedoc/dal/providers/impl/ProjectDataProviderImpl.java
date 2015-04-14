package com.livedoc.dal.providers.impl;

import org.springframework.stereotype.Repository;

import com.livedoc.dal.entities.ProjectEntity;
import com.livedoc.dal.providers.ProjectDataProvider;

@Repository
public class ProjectDataProviderImpl extends
		BaseDataProvider<ProjectEntity, String> implements ProjectDataProvider {

	public ProjectDataProviderImpl() {
		super(ProjectEntity.class);
	}
}

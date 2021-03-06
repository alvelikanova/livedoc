package com.livedoc.dal.providers.impl;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.dal.entities.ProjectEntity;
import com.livedoc.dal.providers.ProjectDataProvider;

@Repository
@Transactional
public class ProjectDataProviderImpl extends
		BaseDataProvider<ProjectEntity, String> implements ProjectDataProvider {

	public ProjectDataProviderImpl() {
		super(ProjectEntity.class);
	}

	public ProjectEntity getProjectByName(String projectName) {
		Session session = sessionFactory.getCurrentSession();
		Criterion criterion = Restrictions.eq("projectName", projectName);
		return (ProjectEntity) session.createCriteria(ProjectEntity.class)
				.add(criterion).uniqueResult();
	}
}

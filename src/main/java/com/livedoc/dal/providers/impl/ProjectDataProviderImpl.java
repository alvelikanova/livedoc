package com.livedoc.dal.providers.impl;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.dal.entities.CategoryEntity;
import com.livedoc.dal.entities.ProjectEntity;
import com.livedoc.dal.providers.CategoryDataProvider;
import com.livedoc.dal.providers.ProjectDataProvider;

@Repository
@Transactional
public class ProjectDataProviderImpl extends
		BaseDataProvider<ProjectEntity, String> implements ProjectDataProvider {

	@Autowired
	private CategoryDataProvider categoryDataProvider;

	public ProjectDataProviderImpl() {
		super(ProjectEntity.class);
	}

	public ProjectEntity getProjectByName(String projectName) {
		Session session = sessionFactory.getCurrentSession();
		Criterion criterion = Restrictions.eq("projectName", projectName);
		return (ProjectEntity) session.createCriteria(ProjectEntity.class)
				.add(criterion).uniqueResult();
	}

	public void deleteProject(ProjectEntity project) {
		Set<CategoryEntity> categories = project.getCategories();
		for (CategoryEntity category : categories) {
			categoryDataProvider.deleteCategory(category);
		}
		this.delete(project);
	}

	public ProjectEntity saveProject(ProjectEntity project) {
		Set<CategoryEntity> categories = project.getCategories();
		for (CategoryEntity category : categories) {
			categoryDataProvider.saveCategory(category);
		}
		return this.saveOrUpdate(project);
	}
}

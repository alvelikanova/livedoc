package com.livedoc.bl.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.services.ProjectService;
import com.livedoc.dal.entities.CategoryEntity;
import com.livedoc.dal.entities.ProjectEntity;
import com.livedoc.dal.providers.CategoryDataProvider;
import com.livedoc.dal.providers.ProjectDataProvider;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectDataProvider projectDataProvider;
	@Autowired
	private CategoryDataProvider categoryDataProvider;
	@Autowired
	private DozerBeanMapper mapper;

	public Project findProjectById(String id) {
		ProjectEntity entity = projectDataProvider.findById(id);
		if (entity != null) {
			Project project = mapper.map(entity, Project.class);
			List<CategoryEntity> categoryEntities = categoryDataProvider
					.findAllCategoriesByProjectId(entity.getProjectId());
			for (CategoryEntity categoryEntity : categoryEntities) {
				Category category = mapper.map(categoryEntity, Category.class);
				project.getCategories().add(category);
			}
			return project;
		}
		return null;
	}

	public List<Project> findAllProjects() {
		List<Project> projects = new ArrayList<Project>();
		List<ProjectEntity> projectEntities = projectDataProvider.findAll();
		for (ProjectEntity projectEntity : projectEntities) {
			Project project = mapper.map(projectEntity, Project.class);
			List<CategoryEntity> categoryEntities = categoryDataProvider
					.findAllCategoriesByProjectId(projectEntity.getProjectId());
			for (CategoryEntity categoryEntity : categoryEntities) {
				Category category = mapper.map(categoryEntity, Category.class);
				project.getCategories().add(category);
			}
			projects.add(project);
		}
		return projects;
	}

	public void saveProject(Project project) {
		ProjectEntity projectEntity = mapper.map(project, ProjectEntity.class);
		projectDataProvider.saveOrUpdate(projectEntity);
	}

}

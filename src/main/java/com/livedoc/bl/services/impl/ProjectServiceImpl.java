package com.livedoc.bl.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.bl.common.MessageException;
import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.services.ProjectService;
import com.livedoc.bl.services.SearchService;
import com.livedoc.dal.entities.ProjectEntity;
import com.livedoc.dal.providers.CategoryDataProvider;
import com.livedoc.dal.providers.ProjectDataProvider;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

	private static final Logger logger = Logger
			.getLogger(ProjectServiceImpl.class);

	@Autowired
	private ProjectDataProvider projectDataProvider;
	@Autowired
	private CategoryDataProvider categoryDataProvider;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private SearchService searchService;

	public Project findProjectById(String id) {
		ProjectEntity entity = projectDataProvider.findById(id);
		if (entity != null) {
			Project project = mapper.map(entity, Project.class);
			return project;
		}
		return null;
	}

	public List<Project> findAllProjects() {
		List<Project> projects = new ArrayList<Project>();
		List<ProjectEntity> projectEntities = projectDataProvider.findAll();
		for (ProjectEntity projectEntity : projectEntities) {
			Project project = mapper.map(projectEntity, Project.class);
			projects.add(project);
		}
		return projects;
	}

	public void saveProject(Project project) {
		ProjectEntity projectEntity = mapper.map(project, ProjectEntity.class);
		projectDataProvider.saveOrUpdate(projectEntity);
	}

	public void deleteProject(Project project) {
		if (project == null || StringUtils.isEmpty(project.getId())) {
			logger.warn("Null project or project with empty id is received");
			return;
		}
		ProjectEntity projectEntity = mapper.map(project, ProjectEntity.class);
		projectDataProvider.delete(projectEntity);
		try {
			searchService.deleteAllIndicesOfProject(project);
		} catch (MessageException e) {
			logger.error("Failed to delete indices of project with id: "
					+ project.getId());
		}
	}

	public boolean checkProjectNameUniqueness(Project project) {
		ProjectEntity projectEntity = projectDataProvider
				.getProjectByName(project.getName());
		if (projectEntity != null) {
			return projectEntity.getProjectId().equals(project.getId());
		}
		return true;
	}

}

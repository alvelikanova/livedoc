package com.livedoc.bl.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.services.ProjectService;
import com.livedoc.dal.entities.ProjectEntity;
import com.livedoc.dal.providers.ProjectDataProvider;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectDataProvider projectDataProvider;
	@Autowired
	private DozerBeanMapper mapper;

	@Transactional
	public Project findProjectById(String id) {
		ProjectEntity entity = projectDataProvider.findById(id);
		if (entity != null) {
			return mapper.map(entity, Project.class);
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

}

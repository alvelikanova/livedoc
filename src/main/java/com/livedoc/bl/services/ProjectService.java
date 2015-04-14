package com.livedoc.bl.services;

import java.util.List;

import com.livedoc.bl.domain.entities.Project;

public interface ProjectService {
	
	Project findProjectById(String id);

	List<Project> findAllProjects();
	
	void saveProject(Project project);
}

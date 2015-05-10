package com.livedoc.dal.providers;

import com.livedoc.dal.entities.ProjectEntity;

public interface ProjectDataProvider extends
		GenericDataProvider<ProjectEntity, String> {

	ProjectEntity getProjectByName(String projectName);

	void deleteProject(ProjectEntity project);

	ProjectEntity saveProject(ProjectEntity project);
}

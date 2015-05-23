package com.livedoc.bl.services;

import java.util.List;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.Project;

public interface CategoryService {

	void deleteCategory(Category category);

	List<Category> getProjectCategories(Project project);
	
	List<Category> getNonEmptyProjectCategories(Project project);

}

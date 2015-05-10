package com.livedoc.dal.providers;

import java.util.List;

import com.livedoc.dal.entities.CategoryEntity;

public interface CategoryDataProvider extends
		GenericDataProvider<CategoryEntity, String> {
	
	List<CategoryEntity> findAllCategoriesByProjectId(String projectId);

	void deleteCategory(CategoryEntity category);

	CategoryEntity saveCategory(CategoryEntity category);
	
}

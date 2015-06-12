package com.livedoc.bl.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.services.CategoryService;
import com.livedoc.dal.entities.CategoryEntity;
import com.livedoc.dal.providers.CategoryDataProvider;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDataProvider categoryDataProvider;
	@Autowired
	private DozerBeanMapper mapper;

	public void deleteCategory(Category category) {
		CategoryEntity categoryEntity = mapper.map(category,
				CategoryEntity.class);
		categoryDataProvider.delete(categoryEntity);
	}

	public List<Category> getProjectCategories(Project project) {
		List<Category> categories = new ArrayList<Category>();
		List<CategoryEntity> categoryEntities = categoryDataProvider
				.findAllCategoriesByProjectId(project.getId());
		for (CategoryEntity entity : categoryEntities) {
			Category caterory = mapper.map(entity, Category.class);
			categories.add(caterory);
		}
		return categories;
	}

	public List<Category> getNonEmptyProjectCategories(Project project) {
		List<Category> categories = new ArrayList<Category>();
		List<CategoryEntity> categoryEntities = categoryDataProvider
				.findAllCategoriesByProjectId(project.getId());
		for (CategoryEntity entity : categoryEntities) {
			if (!CollectionUtils.isEmpty(entity.getDocumentDataList())) {
				Category caterory = mapper.map(entity, Category.class);
				categories.add(caterory);
			}
		}
		return categories;
	}
}

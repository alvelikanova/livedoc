package com.livedoc.dal.providers.impl;

import com.livedoc.dal.entities.CategoryEntity;
import com.livedoc.dal.providers.CategoryDataProvider;

public class CategoryDataProviderImpl extends BaseDataProvider<CategoryEntity, String> implements CategoryDataProvider {

	public CategoryDataProviderImpl() {
		super(CategoryEntity.class);
	}
}

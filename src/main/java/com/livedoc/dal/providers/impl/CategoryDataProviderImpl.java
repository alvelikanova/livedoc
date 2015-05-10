package com.livedoc.dal.providers.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.dal.entities.CategoryEntity;
import com.livedoc.dal.entities.DocumentDataEntity;
import com.livedoc.dal.providers.CategoryDataProvider;
import com.livedoc.dal.providers.DocumentDataProvider;

@Repository
@Transactional
public class CategoryDataProviderImpl extends
		BaseDataProvider<CategoryEntity, String> implements
		CategoryDataProvider {

	@Autowired
	private DocumentDataProvider documentDataProvider;

	public CategoryDataProviderImpl() {
		super(CategoryEntity.class);
	}

	public List<CategoryEntity> findAllCategoriesByProjectId(String projectId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(CategoryEntity.class).add(
				Restrictions.eq("project.projectId", projectId));
		return criteria.list();
	}

	public void deleteCategory(CategoryEntity category) {
		Set<DocumentDataEntity> documents = category.getDocumentDataList();
		for (DocumentDataEntity document : documents) {
			documentDataProvider.deleteDocument(document);
		}
		this.delete(category);
	}

	public CategoryEntity saveCategory(CategoryEntity category) {
		CategoryEntity categoryEntity = this.saveOrUpdate(category);
		Set<DocumentDataEntity> documents = category.getDocumentDataList();
		for (DocumentDataEntity document : documents) {
			documentDataProvider.saveDocument(document);
		}
		return categoryEntity;
	}
}

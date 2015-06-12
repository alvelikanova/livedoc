package com.livedoc.dal.providers.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.dal.entities.CategoryEntity;
import com.livedoc.dal.providers.CategoryDataProvider;

@Repository
@Transactional
public class CategoryDataProviderImpl extends
		BaseDataProvider<CategoryEntity, String> implements
		CategoryDataProvider {

	public CategoryDataProviderImpl() {
		super(CategoryEntity.class);
	}

	@SuppressWarnings("unchecked")
	public List<CategoryEntity> findAllCategoriesByProjectId(String projectId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(CategoryEntity.class).add(
				Restrictions.eq("project.projectId", projectId));
		criteria.addOrder(Order.asc("categoryName"));
		return criteria.list();
	}
}

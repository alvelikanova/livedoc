package com.livedoc.dal.providers.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.dal.entities.DocumentDataEntity;
import com.livedoc.dal.providers.DocumentDataProvider;

@Repository
@Transactional
public class DocumentDataProviderImpl extends
		BaseDataProvider<DocumentDataEntity, String> implements
		DocumentDataProvider {

	public DocumentDataProviderImpl() {
		super(DocumentDataEntity.class);
	}

	@SuppressWarnings("unchecked")
	public List<DocumentDataEntity> getDocumentsByCategoryId(String categoryId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(DocumentDataEntity.class)
				.add(Restrictions.eq("category.categoryId", categoryId));
		criteria.addOrder(Order.asc("documentTitle"));
		return criteria.list();
	}

	public int countDocumentsOfCategory(String categoryId) {
		Session session = getSession();
		Query q = session
				.createQuery("select count(d.documentDataId) from DocumentDataEntity d where d.category.categoryId = '"
						+ categoryId + "'");
		Long result = (Long) q.uniqueResult();
		return result.intValue();
	}
}

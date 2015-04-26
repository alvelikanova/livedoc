package com.livedoc.dal.providers.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.livedoc.dal.entities.DocumentDataEntity;
import com.livedoc.dal.providers.DocumentDataProvider;

@Repository
public class DocumentDataProviderImpl extends
		BaseDataProvider<DocumentDataEntity, String> implements
		DocumentDataProvider {

	public DocumentDataProviderImpl() {
		super(DocumentDataEntity.class);
	}

	public List<DocumentDataEntity> getDocumentsByCategoryId(String categoryId) {
		Session session = getSession();
		Criteria cr = session.createCriteria(DocumentDataEntity.class).add(
				Restrictions.eq("category.categoryId", categoryId));
		return cr.list();
	}

}

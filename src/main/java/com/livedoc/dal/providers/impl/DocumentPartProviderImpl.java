package com.livedoc.dal.providers.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.dal.entities.DocumentPartEntity;
import com.livedoc.dal.providers.DocumentPartProvider;

@Repository
@Transactional
public class DocumentPartProviderImpl extends
		BaseDataProvider<DocumentPartEntity, String> implements
		DocumentPartProvider {

	public DocumentPartProviderImpl() {
		super(DocumentPartEntity.class);
	}

	public DocumentPartEntity getChapter(String documentId, int order) {
		Session session = sessionFactory.getCurrentSession();
		Criteria cr = session
				.createCriteria(DocumentPartEntity.class)
				.add(Restrictions.eq("documentData.documentDataId", documentId))
				.add(Restrictions.eq("documentPartOrder", order));
		return (DocumentPartEntity) cr.uniqueResult();
	}

}

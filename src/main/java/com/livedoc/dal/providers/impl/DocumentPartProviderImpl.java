package com.livedoc.dal.providers.impl;

import java.util.List;

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

	public List<DocumentPartEntity> getPartsOfDocument(String documentId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria cr = session.createCriteria(DocumentPartEntity.class).add(
				Restrictions.eq("documentData.documentDataId", documentId));
		return cr.list();
	}

}

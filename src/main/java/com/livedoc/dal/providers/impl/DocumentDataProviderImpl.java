package com.livedoc.dal.providers.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.dal.entities.DocumentDataEntity;
import com.livedoc.dal.entities.DocumentPartEntity;
import com.livedoc.dal.providers.DocumentDataProvider;
import com.livedoc.dal.providers.DocumentPartProvider;

@Repository
@Transactional
public class DocumentDataProviderImpl extends
		BaseDataProvider<DocumentDataEntity, String> implements
		DocumentDataProvider {

	@Autowired
	private DocumentPartProvider documentPartProvider;

	public DocumentDataProviderImpl() {
		super(DocumentDataEntity.class);
	}

	public List<DocumentDataEntity> getDocumentsByCategoryId(String categoryId) {
		Session session = getSession();
		Criteria cr = session.createCriteria(DocumentDataEntity.class).add(
				Restrictions.eq("category.categoryId", categoryId));
		return cr.list();
	}

	public Long countDocumentsOfCategory(String categoryId) {
		Session session = getSession();
		Query q = session
				.createQuery("select count(d.documentDataId) from DocumentDataEntity d where d.category.categoryId = '"
						+ categoryId + "'");
		return (Long) q.uniqueResult();
	}

	public void deleteDocument(DocumentDataEntity documentData) {
		List<DocumentPartEntity> parts = documentPartProvider
				.getPartsOfDocument(documentData.getDocumentDataId());
		for (DocumentPartEntity part : parts) {
			documentPartProvider.delete(part);
		}
		this.delete(documentData);
	}

	public DocumentDataEntity saveDocument(DocumentDataEntity documentData) {
		DocumentDataEntity docDataEntity = this.saveOrUpdate(documentData);
		Set<DocumentPartEntity> parts = documentData.getParts();
		for (DocumentPartEntity part : parts) {
			documentPartProvider.saveOrUpdate(part);
		}
		return docDataEntity;
	}
}

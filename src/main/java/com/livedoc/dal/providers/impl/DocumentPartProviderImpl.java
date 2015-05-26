package com.livedoc.dal.providers.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
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

	/**
	 * @see com.livedoc.dal.providers.DocumentPartProvider
	 */
	@SuppressWarnings("unchecked")
	public List<DocumentPartEntity> getPartsOfDocument(String documentId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("from DocumentPartEntity p where p.documentData.documentDataId = :documentId order by p.documentPartOrder asc");
		query.setParameter("documentId", documentId);
		return query.list();
	}

	public int countChapters(String documentId) {
		// valid docbook document may start with one metainformation element
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("from DocumentPartEntity p where p.documentData.documentDataId = :documentDataId order by p.documentPartOrder asc");
		query.setParameter("documentDataId", documentId);
		List<DocumentPartEntity> chapters = query.list();
		return chapters.size();
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

package com.livedoc.bl.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.services.DocumentService;
import com.livedoc.dal.entities.DocumentDataEntity;
import com.livedoc.dal.entities.DocumentPartEntity;
import com.livedoc.dal.providers.DocumentDataProvider;
import com.livedoc.dal.providers.DocumentPartProvider;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

	private static final Logger logger = Logger
			.getLogger(DocumentServiceImpl.class);

	@Autowired
	private DocumentDataProvider documentDataProvider;
	@Autowired
	private DocumentPartProvider documentPartProvider;
	@Autowired
	private DozerBeanMapper mapper;

	public DocumentData saveDocument(DocumentData documentData) {
		boolean newlyCreated = documentData.getId() == null;
		if (newlyCreated) {
			documentData.setCreateDate(new Date());
			documentData.setLastModDate(documentData.getCreateDate());
		} else {
			documentData.setLastModDate(new Date());
		}
		DocumentDataEntity documentEntity = mapper.map(documentData,
				DocumentDataEntity.class);
		documentEntity = documentDataProvider.saveOrUpdate(documentEntity);
		return mapper.map(documentEntity, DocumentData.class);
	}

	public List<DocumentData> getDocumentsByCategory(Category category) {
		List<DocumentData> documents = new ArrayList<DocumentData>();
		List<DocumentDataEntity> entities = documentDataProvider
				.getDocumentsByCategoryId(category.getId());
		for (DocumentDataEntity entity : entities) {
			DocumentData document = mapper.map(entity, DocumentData.class);
			documents.add(document);
		}
		return documents;
	}

	public void deleteDocument(DocumentData document) {
		if (document != null && document.getId() != null) {
			DocumentDataEntity documentEntity = mapper.map(document,
					DocumentDataEntity.class);
			documentDataProvider.delete(documentEntity);
		}
	}

	public String getChapterId(DocumentData documentData, int order) {
		DocumentPartEntity chapter = documentPartProvider.getChapter(
				documentData.getId(), order);
		return chapter == null ? null : chapter.getDocumentPartId();
	}
}

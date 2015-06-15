package com.livedoc.bl.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.bl.common.MessageException;
import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.domain.entities.DocumentPart;
import com.livedoc.bl.services.DocumentService;
import com.livedoc.bl.services.SearchService;
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
	@Autowired
	private SearchService searchService;

	public DocumentData saveDocument(DocumentData documentData)
			throws MessageException {
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
		DocumentData result = mapper.map(documentEntity, DocumentData.class);
		if (newlyCreated) {
			searchService.addDocumentToIndex(result);
		} else {
			searchService.updateIndexedDocument(result);
		}
		return result;
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

	public void deleteDocument(DocumentData document) throws MessageException {
		if (document != null && document.getId() != null) {
			DocumentDataEntity documentEntity = mapper.map(document,
					DocumentDataEntity.class);
			documentDataProvider.delete(documentEntity);
			searchService.removeDocumentFromIndex(document);
		}
	}

	public String getChapterId(DocumentData documentData, int order) {
		DocumentPartEntity chapter = documentPartProvider.getChapter(
				documentData.getId(), order);
		return chapter == null ? null : chapter.getDocumentPartId();
	}

	public DocumentData getDocumentById(String documentId) {
		if (documentId == null) {
			logger.warn("Null documentId was received");
			return null;
		}
		DocumentDataEntity entity = documentDataProvider.findById(documentId);
		DocumentData docData = mapper.map(entity, DocumentData.class);
		return docData;
	}

	public DocumentPart getDocumentPartById(String documentPartId) {
		if (documentPartId == null) {
			logger.warn("Null documentPartId was received");
			return null;
		}
		DocumentPartEntity entity = documentPartProvider
				.findById(documentPartId);
		DocumentPart docPart = mapper.map(entity, DocumentPart.class);
		return docPart;
	}
}

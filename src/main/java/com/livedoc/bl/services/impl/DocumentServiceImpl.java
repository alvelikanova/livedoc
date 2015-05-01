package com.livedoc.bl.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.services.DocumentService;
import com.livedoc.dal.entities.DocumentDataEntity;
import com.livedoc.dal.providers.DocumentDataProvider;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	private DocumentDataProvider documentDataProvider;
	@Autowired
	private DozerBeanMapper mapper;

	public void saveDocument(DocumentData document) {
		if (document.getId() == null) {
			document.setCreateDate(new Date());
			document.setLastModDate(document.getCreateDate());
		} else {
			document.setLastModDate(new Date());
		}
		DocumentDataEntity documentEntity = mapper.map(document,
				DocumentDataEntity.class);
		documentDataProvider.saveOrUpdate(documentEntity);
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

	public Long countDocumentsOfCategory(Category category) {
		return documentDataProvider.countDocumentsOfCategory(category.getId());
	}

	public void deleteDocument(DocumentData document) {
		if (document != null && document.getId() != null) {
			DocumentDataEntity documentEntity = mapper.map(document,
					DocumentDataEntity.class);
			documentDataProvider.delete(documentEntity);
		}
	}

}

package com.livedoc.bl.services;

import java.util.List;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;

public interface DocumentService {

	DocumentData saveDocument(DocumentData documentData);

	List<DocumentData> getDocumentsByCategory(Category category);

	void deleteDocument(DocumentData document);

	String getChapterId(DocumentData documentData, int order);

}

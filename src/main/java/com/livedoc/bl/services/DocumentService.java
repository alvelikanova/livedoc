package com.livedoc.bl.services;

import java.util.List;

import org.dom4j.Document;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;

public interface DocumentService {

	/**
	 * Saves document
	 * 
	 * @param documentData
	 * @param document
	 * @return saved document
	 */
	DocumentData saveDocument(DocumentData documentData, Document document);

	List<DocumentData> getDocumentsByCategory(Category category);

	Long countDocumentsOfCategory(Category category);

	void deleteDocument(DocumentData document);
}

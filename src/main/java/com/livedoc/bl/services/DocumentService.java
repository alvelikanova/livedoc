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

	void deleteDocument(DocumentData document);

	Document buildDocument(DocumentData documentData);

	Document buildChapter(DocumentData documentData, int order);

	String getChapterId(DocumentData documentData, int order);

	DocumentData getFullDocument(String docDataId);

	int countDocumentChapters(DocumentData documentData);

	List<Document> getChapters(DocumentData documentData);
}

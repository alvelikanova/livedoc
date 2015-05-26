package com.livedoc.dal.providers;

import java.util.List;

import com.livedoc.dal.entities.DocumentPartEntity;

public interface DocumentPartProvider extends
		GenericDataProvider<DocumentPartEntity, String> {

	/**
	 * Returns list of document parts sorted by order field
	 * 
	 * @param documentId
	 * @return list of document parts
	 */
	List<DocumentPartEntity> getPartsOfDocument(String documentId);

	int countChapters(String documentId);

	DocumentPartEntity getChapter(String documentId, int order);
}

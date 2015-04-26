package com.livedoc.bl.services;

import java.util.List;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;

public interface DocumentService {
	void saveDocument(DocumentData document);

	List<DocumentData> getDocumentsByCategory(Category category);
}

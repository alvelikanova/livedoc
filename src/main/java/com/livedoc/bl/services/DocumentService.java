package com.livedoc.bl.services;

import java.util.List;

import com.livedoc.bl.common.MessageException;
import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.domain.entities.DocumentPart;

public interface DocumentService {

    DocumentData saveDocument(DocumentData documentData)
	    throws MessageException;

    List<DocumentData> getDocumentsByCategory(Category category);

    void deleteDocument(DocumentData document) throws MessageException;

    String getChapterId(DocumentData documentData, int order);

    DocumentData getDocumentById(String documentId);

    DocumentPart getDocumentPartById(String documentPartId);

}

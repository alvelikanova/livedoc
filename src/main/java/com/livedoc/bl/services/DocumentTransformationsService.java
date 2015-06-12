package com.livedoc.bl.services;

import java.util.List;

import org.dom4j.Document;

import com.livedoc.bl.common.MessageException;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.domain.entities.DocumentPart;

public interface DocumentTransformationsService {

	String transformXMLToString(Document document) throws MessageException;

	Document transformToDocument(DocumentData documentData)
			throws MessageException;

	Document transformToChapter(DocumentPart documentPart)
			throws MessageException;

	List<Document> getChapters(DocumentData documentData)
			throws MessageException;

	DocumentData updateDomainDocument(DocumentData documentData,
			Document document);
}

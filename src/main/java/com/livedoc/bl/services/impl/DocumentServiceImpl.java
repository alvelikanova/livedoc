package com.livedoc.bl.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${xml.encoding}")
	private String xmlEncoding;
	@Value("${docbook.version}")
	private String docbookVersion;
	@Value("${docbook.xmlns}")
	private String docbookXmlns;

	public DocumentData saveDocument(DocumentData documentData,
			Document document) {
		boolean newlyCreated = documentData.getId() == null;
		if (newlyCreated) {
			documentData.setCreateDate(new Date());
			documentData.setLastModDate(documentData.getCreateDate());
		} else {
			documentData.setLastModDate(new Date());
		}
		DocumentDataEntity documentEntity = mapper.map(documentData,
				DocumentDataEntity.class);
		if (!newlyCreated) {
			// TODO - how to handle content changing?
			List<DocumentPartEntity> parts = documentPartProvider
					.getPartsOfDocument(documentEntity.getDocumentDataId());
			for (DocumentPartEntity part : parts) {
				documentPartProvider.delete(part);
			}
		}
		Element rootElement = document.getRootElement();
		documentEntity.setRootElementType(rootElement.getName());
		int index = 0;
		for (Iterator<?> i = rootElement.elementIterator(); i.hasNext(); index++) {
			Element element = (Element) i.next();
			DocumentPartEntity partEntity = new DocumentPartEntity();
			partEntity.setDocumentData(documentEntity);
			partEntity.setDocumentPartContent(element.asXML());
			partEntity.setDocumentPartOrder(index);
			documentEntity.getParts().add(partEntity);
		}
		documentEntity = documentDataProvider.saveDocument(documentEntity);
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
			documentDataProvider.deleteDocument(documentEntity);
		}
	}

	public Document buildDocument(DocumentData documentData) {
		Document document = DocumentHelper.createDocument();
		List<DocumentPartEntity> parts = documentPartProvider
				.getPartsOfDocument(documentData.getId());
		document.setXMLEncoding(xmlEncoding);
		Element root = document.addElement(documentData.getRootElement(),
				docbookXmlns).addAttribute("version", docbookVersion);
		Collections.sort(parts, new Comparator<DocumentPartEntity>() {

			public int compare(DocumentPartEntity o1, DocumentPartEntity o2) {
				return o1.getDocumentPartOrder() - o2.getDocumentPartOrder();
			}
		});
		try {
			for (DocumentPartEntity part : parts) {
				Element elem = DocumentHelper.parseText(
						part.getDocumentPartContent()).getRootElement();
				root.add(elem);
			}
		} catch (DocumentException ex) {
			logger.error("Error occured while trying to parse database data: "
					+ ex);
		}
		return document;
	}

	public DocumentData getFullDocument(String docDataId) {
		DocumentDataEntity docDataEntity = documentDataProvider
				.findById(docDataId);
		DocumentData docData = mapper.map(docDataEntity, DocumentData.class);
		docData.setDocument(buildDocument(docData));
		return docData;
	}
}

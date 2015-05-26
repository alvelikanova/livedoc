package com.livedoc.bl.services.impl;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.livedoc.bl.services.DocumentTransformationsService;

@Service
public class DocumentTransformationsServiceImpl implements
		DocumentTransformationsService {
	private static final Logger logger = Logger
			.getLogger(DocumentTransformationsServiceImpl.class);

	@Autowired
	private Transformer transformer;

	public String transformXMLToString(Document document) {
		if (document != null) {
			try {
				DocumentSource source = new DocumentSource(document);
				DocumentResult result = new DocumentResult();
				transformer.transform(source, result);
				Document transformedDocument = result.getDocument();
				return transformedDocument.asXML();
			} catch (TransformerException ex) {
				logger.error(String.format(
						"Error occured while transforming document: %s",
						ex.getMessage()));
			}
		}
		return null;
	}
}

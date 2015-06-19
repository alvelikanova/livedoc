package com.livedoc.bl.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.dom.DOMAttribute;
import org.dom4j.dom.DOMText;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.livedoc.bl.common.MessageException;
import com.livedoc.bl.domain.entities.Comment;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.domain.entities.DocumentPart;
import com.livedoc.bl.services.DocumentTransformationsService;

@Service
public class DocumentTransformationsServiceImpl implements
		DocumentTransformationsService {
	private static final Logger logger = Logger
			.getLogger(DocumentTransformationsServiceImpl.class);

	private static final String[] VALID_ROOT_ELEMENTS = new String[] {
			"acknowledgements", "dedication", "reference", "sect3", "appendix",
			"glossary", "refsect1", "sect4", "article", "index", "refsect2",
			"sect5", "bibliography", "para", "refsect3", "section", "book",
			"part", "refsection", "set", "chapter", "preface", "sect1",
			"setindex", "colophon", "refentry", "sect2", "toc" };
	private static final String CANNOT_TRANSFORM = "DOC_ERROR-001";
	private static final String CANNOT_PARSE = "DOC_ERROR-002";

	@Value("${xml.encoding}")
	private String xmlEncoding;
	@Value("${docbook.version}")
	private String docbookVersion;
	@Value("${docbook.xmlns}")
	private String docbookXmlns;

	@Autowired
	private Transformer transformer;

	public String transformXMLToString(Document document)
			throws MessageException {
		if (document == null) {
			logger.warn("Null document has been retreived");
			return null;
		}
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
			MessageException me = new MessageException();
			me.setMessageCode(CANNOT_TRANSFORM);
			throw me;
		}
	}

	public Document transformToDocument(DocumentData documentData)
			throws MessageException {
		if (documentData == null) {
			logger.warn("Null document has been retreived");
			return null;
		}
		Document document = DocumentHelper.createDocument();
		List<DocumentPart> parts = documentData.getParts();
		document.setXMLEncoding(xmlEncoding);
		Element root = document.addElement(documentData.getRootElement(),
				docbookXmlns).addAttribute("version", docbookVersion);
		try {
			for (DocumentPart part : parts) {
				Element elem = DocumentHelper.parseText(part.getContent())
						.getRootElement();
				root.add(elem);
			}
			return document;
		} catch (DocumentException ex) {
			logger.error("Error occured while trying to parse database data: "
					+ ex);
			MessageException me = new MessageException();
			me.setMessageCode(CANNOT_PARSE);
			throw me;
		}
	}

	public Document transformToChapter(DocumentPart documentPart)
			throws MessageException {
		if (documentPart == null) {
			logger.warn("Null document part has been retreived");
			return null;
		}
		try {
			Document document = DocumentHelper.parseText(documentPart
					.getContent());
			document.setXMLEncoding(xmlEncoding);
			return document;
		} catch (DocumentException ex) {
			logger.error("Error occured while trying to parse database data: "
					+ ex);
			MessageException me = new MessageException();
			me.setMessageCode(CANNOT_PARSE);
			throw me;
		}
	}

	public List<Document> getChapters(DocumentData documentData)
			throws MessageException {
		if (documentData == null) {
			logger.warn("Null document has been retreived");
			return null;
		}
		List<Document> chapters = new ArrayList<Document>();
		List<DocumentPart> parts = documentData.getParts();
		Collections.sort(parts, new Comparator<DocumentPart>() {

			public int compare(DocumentPart o1, DocumentPart o2) {
				return o1.getOrder() - o2.getOrder();
			}
		});
		try {
			for (DocumentPart part : parts) {
				if (Arrays.asList(VALID_ROOT_ELEMENTS).contains(
						part.getRootElementType())) {
					Document document = DocumentHelper.parseText(part
							.getContent());
					document.setXMLEncoding(xmlEncoding);
					chapters.add(document);
				}
			}
		} catch (DocumentException ex) {
			logger.error("Error occured while trying to parse database data: "
					+ ex);
			MessageException me = new MessageException();
			me.setMessageCode(CANNOT_PARSE);
			throw me;
		}
		return chapters;
	}

	public DocumentData updateDomainDocument(DocumentData documentData,
			Document document) {
		if (documentData == null) {
			logger.warn("Null domain document has been retreived");
			return null;
		}
		if (document == null) {
			logger.warn("Null dom4j.document has been retreived");
			return documentData;
		}
		Element rootElement = document.getRootElement();
		documentData.setRootElement(rootElement.getName());
		documentData.setParts(new ArrayList<DocumentPart>());
		int index = 0;
		for (Iterator<?> i = rootElement.elementIterator(); i.hasNext(); index++) {
			Element element = (Element) i.next();
			DocumentPart part = new DocumentPart();
			part.setDocumentData(documentData);
			part.setContent(element.asXML());
			part.setOrder(index);
			part.setRootElementType(element.getName());
			documentData.getParts().add(part);
		}
		return documentData;
	}

	@Override
	public String getDocumentWithComments(DocumentData documentData)
			throws MessageException {
		if (documentData == null) {
			logger.warn("Null document has been retreived");
			return null;
		}
		Document document = DocumentHelper.createDocument();
		List<DocumentPart> parts = documentData.getParts();
		document.setXMLEncoding(xmlEncoding);
		Element root = document.addElement(documentData.getRootElement(),
				docbookXmlns).addAttribute("version", docbookVersion);
		try {
			for (DocumentPart part : parts) {
				Element elem = DocumentHelper.parseText(part.getContent())
						.getRootElement();
				List<Comment> comments = part.getComments();
				for (Comment comment : comments) {
					Element remark = elem.addElement("remark");
					remark.add(new DOMAttribute(new QName("author"), comment
							.getAuthor().getName()));
					remark.add(new DOMText(comment.getComment()));
				}
				root.add(elem);
			}
		} catch (DocumentException ex) {
			logger.error("Error occured while trying to parse database data: "
					+ ex);
			MessageException me = new MessageException();
			me.setMessageCode(CANNOT_PARSE);
			throw me;
		}
		return document.asXML();
	}
}

package com.livedoc.bl.services.impl;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.livedoc.bl.services.DocumentTransformationsService;

@Service
public class DocumentTransformationsServiceImpl implements
		DocumentTransformationsService {
	private static final Logger logger = Logger
			.getLogger(DocumentTransformationsServiceImpl.class);

	// TODO snippet
	public void doit() throws ParserConfigurationException,
			TransformerFactoryConfigurationError, TransformerException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();
		Document doc = impl.createDocument(null, null, null);
		Element e1 = doc.createElement("api");
		doc.appendChild(e1);
		Element e2 = doc.createElement("java");
		e1.appendChild(e2);

		e2.setAttribute("url", "http://www.domain.com");
		DOMSource domSource = new DOMSource(doc);
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StringWriter sw = new StringWriter();
		StreamResult sr = new StreamResult(sw);
		transformer.transform(domSource, sr);
		System.out.println(sw.toString());
	}

	public String transformXMLToString(Document document) {
		DOMSource source = new DOMSource(document);
		// xsl file
		StreamSource xslStream = new StreamSource(this.getClass()
				.getResourceAsStream("/xsl/docbook.xsl"));
		// output
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);

		TransformerFactory factory = TransformerFactory.newInstance();
		try {
			Transformer transformer = factory.newTransformer(xslStream);
			transformer.transform(source, result);
		} catch (TransformerConfigurationException ex) {
			logger.error(String.format(
					"Configuration error, cannot initialize Transformer: %s",
					ex.getMessage()));
		} catch (TransformerException ex) {
			logger.error(String.format(
					"Error occured while transforming document: %s",
					ex.getMessage()));
		}
		return sw.toString();
	}
}

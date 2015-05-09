package com.livedoc.bl.services.impl;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.DOMWriter;
import org.springframework.stereotype.Service;

import com.livedoc.bl.services.DocumentTransformationsService;

@Service
public class DocumentTransformationsServiceImpl implements
		DocumentTransformationsService {
	private static final Logger logger = Logger
			.getLogger(DocumentTransformationsServiceImpl.class);

	public String transformXMLToString(Document document) {
		try {
			DOMSource source = new DOMSource(new DOMWriter().write(document));
			// xsl file
			StreamSource xslStream = new StreamSource(this.getClass()
					.getResourceAsStream("/xsl/docbook.xsl"));
			// output
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);

			TransformerFactory factory = TransformerFactory.newInstance();

			Transformer transformer = factory.newTransformer(xslStream);
			transformer.transform(source, result);
			return sw.toString();
		} catch (TransformerConfigurationException ex) {
			logger.error(String.format(
					"Configuration error, cannot initialize Transformer: %s",
					ex.getMessage()));
		} catch (TransformerException ex) {
			logger.error(String.format(
					"Error occured while transforming document: %s",
					ex.getMessage()));
		} catch (DocumentException ex) {
			logger.error(String.format(
					"Error occured while converting dom4j document: %s",
					ex.getMessage()));
		}
		return null;
	}
}

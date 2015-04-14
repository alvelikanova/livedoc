package com.livedoc.bl.services.impl;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.domain.entities.User;
import com.livedoc.bl.services.DocumentService;
import com.livedoc.dal.entities.DocumentDataEntity;
import com.livedoc.dal.entities.ProjectEntity;
import com.livedoc.dal.entities.UserEntity;
import com.livedoc.dal.providers.DocumentDataProvider;

@Service
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	private DocumentDataProvider documentDataProvider;
	@Autowired
	private DozerBeanMapper mapper;

	@Transactional
	public void saveDocument(DocumentData document) {
		DocumentDataEntity documentEntity = mapper.map(document, DocumentDataEntity.class);

		User user = document.getCreateUser();
		UserEntity userEntity = mapper.map(user, UserEntity.class);
		documentEntity.setCreateUser(userEntity);

		Project project = document.getProject();
		ProjectEntity projectEntity = mapper.map(project, ProjectEntity.class);
		documentEntity.setProject(projectEntity);

		documentDataProvider.saveOrUpdate(documentEntity);
	}

	//TODO snippet
	public void doit() throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {

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
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StringWriter sw = new StringWriter();
		StreamResult sr = new StreamResult(sw);
		transformer.transform(domSource, sr);
		System.out.println(sw.toString());
	}

}

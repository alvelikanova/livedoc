package com.livedoc.ui.administration.documents;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.ValidationError;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.ui.components.Feedback;

public abstract class DocumentUploadPanel extends GenericPanel<DocumentData> {

	private static final long serialVersionUID = 3419403935384133578L;
	private static final Logger logger = Logger.getLogger(DocumentUploadPanel.class);

	private static final String XML_CONTENT_TYPE = "text/xml";
	private FileUploadField fileUploadField;
	private Feedback feedback;
	private IModel<String> htmlModel;

	public DocumentUploadPanel(String id, IModel<DocumentData> model, IModel<String> htmlModel) {
		super(id, model);
		this.htmlModel = htmlModel;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		Form<Void> form = new Form<Void>("form");
		add(form);

		fileUploadField = new FileUploadField("fileUpload");
		fileUploadField.setRequired(true);
		feedback = new Feedback("feedback");
		feedback.setOutputMarkupId(true);

		form.add(feedback, fileUploadField);

		AjaxButton submitButton = new AjaxButton("uploadDocument", form) {

			private static final long serialVersionUID = -1578455314617686312L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				final FileUpload uploadedFile = fileUploadField.getFileUpload();
				target.add(feedback);
				if (!XML_CONTENT_TYPE.equals(uploadedFile.getContentType())) {
					error(new ValidationError(getString("error.notxml")));
					return;
				}
				Document document = parseDocumentFromInput(uploadedFile);
				htmlModel.setObject(transform(document));
				DocumentUploadPanel.this.onSubmit(target);
			}
		};

		form.add(submitButton);
	}

	protected abstract void onSubmit(AjaxRequestTarget target);
	
	private Document parseDocumentFromInput(FileUpload uploadedFile) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			/*
			 * The InputStream return will be closed by Wicket at the end of the
			 * request.
			 */
			InputStream stream = uploadedFile.getInputStream();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(stream);
			Element element = doc.getDocumentElement();
			NodeList nodes = element.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				System.out.println("" + nodes.item(i).getTextContent());
			}
		} catch (ParserConfigurationException ex) {
			logger.error(String.format("Configuration error, cannot initialize DocumentBuilder: %s", ex.getMessage()));
		} catch (SAXException ex) {
			logger.error(String.format("Error while parsing document with name %s: %s",
					uploadedFile.getClientFileName(), ex.getMessage()));
		} catch (IOException ex) {
			logger.error(String.format("An I/O Error occured while getting document with name %s: %s",
					uploadedFile.getClientFileName(), ex.getMessage()));
		}
		return doc;
	}

	private String transform(Document document) {
		// source document in doc book which has to be transformed
		DOMSource source = new DOMSource(document);
		// xsl file
		StreamSource xslStream = new StreamSource(this.getClass().getResourceAsStream("/xsl/docbook.xsl"));
		// output
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);

		TransformerFactory factory = TransformerFactory.newInstance();
		try {
			Transformer transformer = factory.newTransformer(xslStream);
			transformer.transform(source, result);
		} catch (TransformerConfigurationException ex) {
			logger.error(String.format("Configuration error, cannot initialize Transformer: %s", ex.getMessage()));
		} catch (TransformerException ex) {
			logger.error(String.format("Error occured while transforming document: %s", ex.getMessage()));
		}
		return sw.toString();
	}
}

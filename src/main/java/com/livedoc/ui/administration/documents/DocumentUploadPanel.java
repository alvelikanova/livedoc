package com.livedoc.ui.administration.documents;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.services.DocumentTransformationsService;

public abstract class DocumentUploadPanel extends GenericPanel<DocumentData> {

	private static final long serialVersionUID = 3419403935384133578L;
	private static final Logger logger = Logger
			.getLogger(DocumentUploadPanel.class);

	private static final String XML_CONTENT_TYPE = "text/xml";
	private FileUploadField fileUploadField;
	private IModel<String> htmlModel;

	@SpringBean
	private DocumentTransformationsService documentTransformationsService;

	public DocumentUploadPanel(String id, IModel<DocumentData> model,
			IModel<String> htmlModel) {
		super(id, model);
		Injector.get().inject(this);
		this.htmlModel = htmlModel;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		Form<Void> form = new Form<Void>("fileUploadForm");
		add(form);

		fileUploadField = new FileUploadField("fileUpload");

		form.add(fileUploadField);
		fileUploadField.add(new IValidator<List<FileUpload>>() {

			private static final long serialVersionUID = 3294459995272626844L;

			public void validate(IValidatable<List<FileUpload>> validatable) {
				FileUpload uploadedFile = fileUploadField.getFileUpload();
				if (!XML_CONTENT_TYPE.equals(uploadedFile.getContentType())) {
					error(getString("error.notxml"));
				}
			}
		});

		AjaxButton submitButton = new AjaxButton("uploadDocument", form) {

			private static final long serialVersionUID = -1578455314617686312L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Document document = parseDocumentFromInput(fileUploadField
						.getFileUpload());
				htmlModel.setObject(documentTransformationsService
						.transformXMLToString(document));
				DocumentUploadPanel.this.onSubmit(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				DocumentUploadPanel.this.onError(target);
			}
		};

		form.add(submitButton);
	}

	protected abstract void onSubmit(AjaxRequestTarget target);

	protected abstract void onError(AjaxRequestTarget target);

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
			logger.error(String
					.format("Configuration error, cannot initialize DocumentBuilder: %s",
							ex.getMessage()));
		} catch (SAXException ex) {
			logger.error(String.format(
					"Error while parsing document with name %s: %s",
					uploadedFile.getClientFileName(), ex.getMessage()));
		} catch (IOException ex) {
			logger.error(String
					.format("An I/O Error occured while getting document with name %s: %s",
							uploadedFile.getClientFileName(), ex.getMessage()));
		}
		return doc;
	}
}

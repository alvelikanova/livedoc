package com.livedoc.ui.administration.documents;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.services.DocumentService;
import com.livedoc.bl.services.DocumentTransformationsService;
import com.livedoc.ui.common.components.Feedback;
import com.livedoc.ui.common.components.MarkupProviderPanel;
import com.livedoc.ui.common.components.MessageDialogContent;
import com.livedoc.ui.common.components.ModalDialog;
import com.livedoc.ui.pages.MasterPage;

public class EditDocumentPage extends MasterPage {

	private static final long serialVersionUID = 5122942399253204854L;
	private static final Logger logger = Logger
			.getLogger(EditDocumentPage.class);

	// services
	@SpringBean
	private DocumentService documentService;
	@SpringBean
	private DocumentTransformationsService documentTransformationsService;

	// models
	private IModel<DocumentData> documentDataModel;
	private Model<String> htmlModel;
	private Document document;

	// components
	private MarkupProviderPanel markupPanel;
	private RequiredTextField<String> titleTextField;
	private TextArea<String> descriptionTextField;
	private WebMarkupContainer previewContainer;
	private Feedback feedbackPanel;
	private FileUploadField fileUploadField;
	private AjaxButton uploadDocumentButton;
	private Form<Document> fileUploadForm;
	private ModalDialog dialog;

	// pages
	private WebPage pageToReturn;

	// constants
	private static final String XML_CONTENT_TYPE = "text/xml";

	public EditDocumentPage(WebPage pageToReturn,
			IModel<DocumentData> documentDataModel) {
		super();
		this.documentDataModel = documentDataModel;
		this.pageToReturn = pageToReturn;
	}

	public void onInitialize() {
		super.onInitialize();

		Form<Void> form = new Form<Void>("form");
		add(form);
		dialog = new ModalDialog("dialog");
		add(dialog);

		feedbackPanel = new Feedback("feedback");
		feedbackPanel.setOutputMarkupId(true);
		form.add(feedbackPanel);

		// document's title
		titleTextField = new RequiredTextField<String>("title",
				new PropertyModel<String>(documentDataModel, "title"));
		titleTextField.add(StringValidator.maximumLength(64));
		// document's description
		descriptionTextField = new TextArea<String>("description",
				new PropertyModel<String>(documentDataModel, "description"));
		descriptionTextField.add(StringValidator.maximumLength(256));
		form.add(titleTextField, descriptionTextField);

		// file upload field
		fileUploadForm = new Form<Document>("fileUploadForm",
				new PropertyModel<Document>(this, "document"));
		form.add(fileUploadForm);
		htmlModel = new Model<String>("");
		fileUploadField = new FileUploadField("fileUpload");
		fileUploadField.setRequired(true);
		fileUploadField.add(new XMLFileValidator());
		uploadDocumentButton = new AjaxButton("uploadDocument", fileUploadForm) {
			private static final long serialVersionUID = -1578455314617686312L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Document document = parseDocumentFromInput(fileUploadField
						.getFileUpload());
				fileUploadForm.setModelObject(document);
				htmlModel.setObject(documentTransformationsService
						.transformXMLToString(document));
				target.add(feedbackPanel, previewContainer);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedbackPanel);
			}
		};
		fileUploadForm.add(fileUploadField, uploadDocumentButton);

		// document's transformed content preview
		previewContainer = new WebMarkupContainer("preview") {
			private static final long serialVersionUID = 6826422158800036268L;

			@Override
			public void onConfigure() {
				super.onConfigure();
				setVisible(!StringUtils.isEmpty(htmlModel.getObject()));
			}
		};
		previewContainer.setOutputMarkupPlaceholderTag(true);
		previewContainer.setOutputMarkupId(true);
		markupPanel = new MarkupProviderPanel("markupPanel", htmlModel);
		markupPanel.setOutputMarkupId(true);
		form.add(previewContainer);
		previewContainer.add(markupPanel);

		// buttons
		AjaxButton saveButton = new AjaxButton("save", form) {

			private static final long serialVersionUID = 5736418898197587665L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (fileUploadForm.getModelObject() == null) {
					Document document = parseDocumentFromInput(fileUploadField
							.getFileUpload());
					fileUploadForm.setModelObject(document);
				}
				documentService.saveDocument(documentDataModel.getObject(),
						fileUploadForm.getModelObject());
				dialog.setTitle(getString("dialog.title"));
				dialog.setContent(new MessageDialogContent(dialog
						.getContentId(), new ResourceModel("save.message"),
						MessageDialogContent.Buttons.OK) {

					private static final long serialVersionUID = -156937675362688835L;

					@Override
					protected void onConfirm(AjaxRequestTarget target) {
						setResponsePage(pageToReturn);
						dialog.close(target);
					}
				});
				dialog.show(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedbackPanel);
			}
		};
		AjaxLink<Void> cancelButton = new AjaxLink<Void>("cancel") {

			private static final long serialVersionUID = 6519349294466919297L;

			@Override
			public void onClick(final AjaxRequestTarget target) {
				dialog.setTitle(getString("dialog.title"));
				dialog.setContent(new MessageDialogContent(
						dialog.getContentId(),
						new ResourceModel(
								documentDataModel.getObject().getId() == null ? "cancel.document-creating.confirm"
										: "cancel.document-editing.confirm"),
						MessageDialogContent.Buttons.OK,
						MessageDialogContent.Buttons.CANCEL) {

					private static final long serialVersionUID = -156937675362688835L;

					@Override
					protected void onConfirm(AjaxRequestTarget target) {
						setResponsePage(pageToReturn);
						dialog.close(target);
					}

					@Override
					protected void onCancel(AjaxRequestTarget target) {
						dialog.close(target);
					}
				});
				dialog.show(target);
			}
		};
		form.add(saveButton, cancelButton);
	}

	/**
	 * Checks if uploaded file has xml format
	 */
	class XMLFileValidator implements IValidator<List<FileUpload>> {
		private static final long serialVersionUID = 3294459995272626844L;

		public void validate(IValidatable<List<FileUpload>> validatable) {
			FileUpload uploadedFile = fileUploadField.getFileUpload();
			if (!XML_CONTENT_TYPE.equals(uploadedFile.getContentType())) {
				error(getString("error.notxml"));
			}
		}
	}

	/**
	 * Builds Document object from uploaded file
	 * 
	 * @param uploadedFile
	 * @return Document
	 */
	private Document parseDocumentFromInput(FileUpload uploadedFile) {
		Document doc = null;
		try {
			/*
			 * The InputStream return will be closed by Wicket at the end of the
			 * request.
			 */
			SAXReader reader = new SAXReader();
			InputStream stream = uploadedFile.getInputStream();
			doc = reader.read(stream);
		} catch (IOException ex) {
			logger.error(String
					.format("An I/O Error occured while getting document with name %s: %s",
							uploadedFile.getClientFileName(), ex.getMessage()));
		} catch (DocumentException ex) {
			logger.error(String
					.format("An error occured while processing document with name %s: %s",
							uploadedFile.getClientFileName(), ex.getMessage()));
		}
		return doc;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}

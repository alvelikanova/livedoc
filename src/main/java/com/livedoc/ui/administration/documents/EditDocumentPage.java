package com.livedoc.ui.administration.documents;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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

import com.livedoc.bl.common.MessageException;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.services.DocumentService;
import com.livedoc.bl.services.DocumentTransformationsService;
import com.livedoc.ui.common.components.Feedback;
import com.livedoc.ui.common.components.MarkupProviderPanel;
import com.livedoc.ui.common.components.dialogs.MessageDialogContent;
import com.livedoc.ui.common.components.dialogs.ModalDialog;
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
	private Model<String> htmlModel = new Model<String>();

	// components
	private MarkupProviderPanel markupPanel;
	private RequiredTextField<String> titleTextField;
	private TextArea<String> descriptionTextField;
	private WebMarkupContainer previewContainer;
	private Feedback feedbackPanel;
	private FileUploadField fileUploadField;
	private AjaxButton uploadDocumentButton;
	private Form<DocumentData> fileUploadForm;
	private ModalDialog dialog;

	// pages
	private WebPage pageToReturn;

	// constants
	private static final String XML_CONTENT_TYPE = "text/xml";
	private static final String CANNOT_LOAD = "DOC_ERROR-003";

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
		fileUploadForm = new Form<DocumentData>("fileUploadForm",
				documentDataModel);
		form.add(fileUploadForm);
		fileUploadField = new FileUploadField("fileUpload");
		fileUploadField
				.setRequired(documentDataModel.getObject().getId() == null);
		fileUploadField.add(new XMLFileValidator());
		uploadDocumentButton = new AjaxButton("uploadDocument", fileUploadForm) {
			private static final long serialVersionUID = -1578455314617686312L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try {
					Document document = parseDocumentFromInput(fileUploadField
							.getFileUpload());
					String html = documentTransformationsService
							.transformXMLToString(document);
					htmlModel.setObject(html);
					DocumentData documentData = documentDataModel.getObject();
					documentDataModel.setObject(documentTransformationsService
							.updateDomainDocument(documentData, document));
					target.add(previewContainer);
				} catch (MessageException e) {
					error(getString(e.getMessageCode()));
				}
				target.add(feedbackPanel);
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
				setVisible(htmlModel.getObject() != null);
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
				DocumentData documentData = documentDataModel.getObject();
				if (CollectionUtils.isEmpty(documentDataModel.getObject()
						.getParts())) {
					Document document;
					try {
						document = parseDocumentFromInput(fileUploadField
								.getFileUpload());
					} catch (MessageException e) {
						error(getString(e.getMessageCode()));
						target.add(feedbackPanel);
						return;
					}
					documentData = documentTransformationsService
							.updateDomainDocument(documentData, document);
				}
				try {
					documentService.saveDocument(documentData);
				} catch (MessageException e) {
					error(getString(e.getMessageCode()));
					onError(target, form);
					return;
				}
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
	 * @throws MessageException
	 */
	private Document parseDocumentFromInput(FileUpload uploadedFile)
			throws MessageException {
		try {
			/*
			 * The InputStream return will be closed by Wicket at the end of the
			 * request.
			 */
			SAXReader reader = new SAXReader();
			InputStream stream = uploadedFile.getInputStream();
			return reader.read(stream);
		} catch (IOException ex) {
			logger.error(String
					.format("An I/O Error occured while getting document with name %s: %s",
							uploadedFile.getClientFileName(), ex.getMessage()));
			MessageException me = new MessageException();
			me.setMessageCode(CANNOT_LOAD);
			throw me;
		} catch (DocumentException ex) {
			logger.error(String
					.format("An error occured while processing document with name %s: %s",
							uploadedFile.getClientFileName(), ex.getMessage()));
			MessageException me = new MessageException();
			me.setMessageCode(CANNOT_LOAD);
			throw me;
		}
	}

}

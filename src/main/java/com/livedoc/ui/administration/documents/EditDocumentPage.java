package com.livedoc.ui.administration.documents;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.validation.validator.StringValidator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.livedoc.bl.common.MessageException;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.services.DocumentService;
import com.livedoc.bl.services.DocumentTransformationsService;
import com.livedoc.ui.common.components.Feedback;
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
	private IModel<String> htmlModel = new Model<String>();

	// components
	private RequiredTextField<String> titleTextField;
	private TextArea<String> descriptionTextField;
	private Feedback feedbackPanel;
	private ModalDialog dialog;
	private DownloadLink downloadButton;
	private DocumentContentPanel documentContentPanel;

	// pages
	private WebPage pageToReturn;

	// constants
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
		form.setMultiPart(true);
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

		documentContentPanel = new DocumentContentPanel("documentContentPanel",
				documentDataModel, htmlModel) {

			private static final long serialVersionUID = -7391524243386967375L;

			@Override
			public void onUpload(AjaxRequestTarget target) {
				try {
					Document document = parseDocumentFromInput(this
							.getFileUpload());
					String html = documentTransformationsService
							.transformXMLToString(document);
					htmlModel.setObject(html);
					DocumentData documentData = documentDataModel.getObject();
					documentDataModel.setObject(documentTransformationsService
							.updateDomainDocument(documentData, document));
				} catch (MessageException e) {
					error(getString(e.getMessageCode()));
				}
				target.add(feedbackPanel);
			}

			@Override
			public void onError(AjaxRequestTarget target) {
				target.add(feedbackPanel);
			}
		};
		form.add(documentContentPanel);
		documentContentPanel
				.setVisible(documentDataModel.getObject().getId() == null);
		documentContentPanel.setOutputMarkupId(true);
		documentContentPanel.setOutputMarkupPlaceholderTag(true);

		AjaxLink<Void> refreshDocumentButton = new AjaxLink<Void>(
				"refreshDocumentButton") {

			private static final long serialVersionUID = 6519349294466919297L;

			@Override
			public void onClick(final AjaxRequestTarget target) {
				documentContentPanel.setVisible(true);
				this.setVisible(false);
				downloadButton.setVisible(false);
				target.add(documentContentPanel, this, downloadButton);
			}
		};
		refreshDocumentButton
				.setVisible(documentDataModel.getObject().getId() != null);
		refreshDocumentButton.setOutputMarkupId(true);

		IModel<File> fileModel = new AbstractReadOnlyModel<File>() {

			private static final long serialVersionUID = 2958509418179825885L;

			@Override
			public File getObject() {
				File file = null;
				try {
					String document = documentTransformationsService
							.getDocumentWithComments(documentDataModel
									.getObject());
					String path = this.getClass().getResource("/tmp/")
							.getFile();
					File dir = new File(path.substring(1, path.length()));
					file = File.createTempFile(documentDataModel.getObject()
							.getId(), ".xml", dir);
					FileOutputStream stream = new FileOutputStream(file);
					BufferedWriter writer = new BufferedWriter(
							new OutputStreamWriter(stream,
									Charset.forName("UTF-8")));
					writer.write(document);
					writer.close();
				} catch (MessageException e) {
					logger.error(e);
				} catch (IOException e) {
					logger.error(e);
				}
				return file;

			}
		};
		downloadButton = new DownloadLink("downloadButton", fileModel) {

			private static final long serialVersionUID = 1712471382919466169L;

			@Override
			public void onClick() {
				IResourceStream resourceStream = new FileResourceStream(
						getModelObject());
				IRequestHandler handler = new ResourceStreamRequestHandler(
						resourceStream, documentDataModel.getObject()
								.getTitle() + ".xml") {
					@Override
					public void respond(IRequestCycle requestCycle) {
						super.respond(requestCycle);
						getModelObject().delete();
					}
				};
				getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
			}
		};
		downloadButton
				.setVisible(documentDataModel.getObject().getId() != null);
		downloadButton.setOutputMarkupId(true);
		form.add(refreshDocumentButton, downloadButton);

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
						document = parseDocumentFromInput(documentContentPanel
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

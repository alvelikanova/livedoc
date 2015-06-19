package com.livedoc.ui.administration.documents;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.ui.common.components.MarkupProviderPanel;

public abstract class DocumentContentPanel extends GenericPanel<DocumentData> {

	private static final long serialVersionUID = 7266706784303899383L;

	// constants
	private static final String XML_CONTENT_TYPE = "text/xml";

	// components
	private Form<DocumentData> fileUploadForm;
	private MarkupProviderPanel markupPanel;
	private FileUploadField fileUploadField;
	private WebMarkupContainer previewContainer;
	private AjaxButton uploadDocumentButton;

	// models
	private IModel<String> htmlModel;

	public DocumentContentPanel(String id, IModel<DocumentData> model,
			IModel<String> htmlModel) {
		super(id, model);
		this.htmlModel = htmlModel;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		// file upload field
		fileUploadForm = new Form<DocumentData>("fileUploadForm", getModel());
		add(fileUploadForm);
		fileUploadField = new FileUploadField("fileUpload");
		fileUploadField.setRequired(getModelObject().getId() == null);
		fileUploadField.add(new XMLFileValidator());
		uploadDocumentButton = new AjaxButton("uploadDocument", fileUploadForm) {
			private static final long serialVersionUID = -1578455314617686312L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				DocumentContentPanel.this.onUpload(target);
				target.add(previewContainer);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				DocumentContentPanel.this.onError(target);
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
		add(previewContainer);
		previewContainer.add(markupPanel);
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

	public FileUpload getFileUpload() {
		return fileUploadField.getFileUpload();
	}

	public abstract void onUpload(AjaxRequestTarget target);

	public abstract void onError(AjaxRequestTarget target);
}

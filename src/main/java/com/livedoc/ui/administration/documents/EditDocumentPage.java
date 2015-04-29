package com.livedoc.ui.administration.documents;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.services.DocumentService;
import com.livedoc.ui.common.components.Feedback;
import com.livedoc.ui.common.components.MarkupProviderPanel;
import com.livedoc.ui.pages.MasterPage;

public class EditDocumentPage extends MasterPage {

	private static final long serialVersionUID = 5122942399253204854L;

	private IModel<DocumentData> model;

	private DocumentUploadPanel documentUploadPanel;
	private MarkupProviderPanel markupPanel;
	private RequiredTextField<String> titleTextField;
	private TextArea<String> descriptionTextField;
	private WebMarkupContainer previewContainer;
	private Feedback feedbackPanel;
	private WebPage pageToReturn;

	@SpringBean
	private DocumentService documentService;

	public EditDocumentPage(WebPage pageToReturn, IModel<DocumentData> model) {
		super();
		this.model = model;
		this.pageToReturn = pageToReturn;
	}

	public void onInitialize() {
		super.onInitialize();

		Form<Void> form = new Form<Void>("form");
		add(form);

		feedbackPanel = new Feedback("feedback");
		feedbackPanel.setOutputMarkupId(true);
		form.add(feedbackPanel);

		titleTextField = new RequiredTextField<String>("title",
				new PropertyModel<String>(model, "title"));
		descriptionTextField = new TextArea<String>("description",
				new PropertyModel<String>(model, "description"));
		form.add(titleTextField, descriptionTextField);

		final Model<String> htmlModel = new Model<String>("");
		documentUploadPanel = new DocumentUploadPanel("doc-upload-panel",
				model, htmlModel) {
			private static final long serialVersionUID = -4399736657121659245L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				target.add(feedbackPanel, previewContainer);
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
				target.add(feedbackPanel, previewContainer);
			}
		};
		form.add(documentUploadPanel);
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
		AjaxButton saveButton = new AjaxButton("save", form) {

			private static final long serialVersionUID = 5736418898197587665L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				documentService.saveDocument(model.getObject());
				setResponsePage(pageToReturn);
			}
		};
		AjaxButton cancelButton = new AjaxButton("cancel") {

			private static final long serialVersionUID = 6519349294466919297L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				setResponsePage(pageToReturn);
			}
		};
		form.add(saveButton, cancelButton);
	}
}

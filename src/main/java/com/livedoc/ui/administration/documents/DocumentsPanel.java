package com.livedoc.ui.administration.documents;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.ui.components.MarkupProviderPanel;

public class DocumentsPanel extends Panel {

	private static final long serialVersionUID = 6022308427662783332L;

	private Model<String> htmlModel;
	private DocumentUploadPanel documentUploadPanel;
	private MarkupProviderPanel markupPanel;

	public DocumentsPanel(String id) {
		super(id);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		htmlModel = new Model<String>("");
		DocumentData document = new DocumentData();
		documentUploadPanel = new DocumentUploadPanel("doc-upload-panel", new Model<DocumentData>(document), htmlModel) {
			private static final long serialVersionUID = -4399736657121659245L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				target.add(markupPanel);
			}
		};
		add(documentUploadPanel);
		markupPanel = new MarkupProviderPanel("markupPanel", htmlModel);
		markupPanel.setOutputMarkupId(true);
		add(markupPanel);
	}
}

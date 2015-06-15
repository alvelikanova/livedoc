package com.livedoc.ui.projects.categories.document;

import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.ui.common.models.DateToStringModel;

public class DocumentInformationPanel extends GenericPanel<DocumentData> {

	private static final long serialVersionUID = -583496850354758231L;

	public DocumentInformationPanel(String id, IModel<DocumentData> model) {
		super(id, model);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		Label documentTitle = new Label("documentTitle",
				new PropertyModel<String>(getModel(), "title"));
		Label documentDescription = new Label("documentDescription",
				new PropertyModel<String>(getModel(), "description"));
		Label createdDate = new Label("createdDate", new DateToStringModel(
				new PropertyModel<Date>(getModel(), "createDate")));
		Label lastModDate = new Label("lastModDate", new DateToStringModel(
				new PropertyModel<Date>(getModel(), "lastModDate")));
		Label author = new Label("author", new PropertyModel<String>(
				getModel(), "createUser.name"));
		Label lastModUser = new Label("lastModUser", new PropertyModel<String>(
				getModel(), "lastModUser.name"));
		add(documentTitle, documentDescription, createdDate, lastModDate,
				author, lastModUser);
	}
}

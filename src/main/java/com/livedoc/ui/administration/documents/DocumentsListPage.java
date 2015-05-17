package com.livedoc.ui.administration.documents;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.ui.common.components.table.Settings;
import com.livedoc.ui.common.components.table.Table;
import com.livedoc.ui.pages.MasterPage;

public class DocumentsListPage extends MasterPage {

	private static final long serialVersionUID = 5595873958504995615L;

	private DocumentProvider documentProvider;
	private Table<DocumentData, String> table;
	private IModel<Category> categoryModel;
	private WebPage pageToReturn;

	public DocumentsListPage(IModel<Category> categoryModel,
			WebPage pageToReturn) {
		super();
		this.categoryModel = categoryModel;
		this.pageToReturn = pageToReturn;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		documentProvider = new DocumentProvider(categoryModel.getObject());
		table = new Table<DocumentData, String>("documents-table",
				prepareSettingsForTable(), documentProvider);
		table.setCellFunctionsProvider(new DocumentCellFunctionsProvider(
				DocumentsListPage.this, table));
		AjaxLink<Void> createDocumentLink = new AjaxLink<Void>(
				"create-document") {

			private static final long serialVersionUID = -3164894736740989842L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				DocumentData documentData = new DocumentData();
				documentData.setCategory(categoryModel.getObject());
				documentData.setCreateUser(getUserData().getUser());
				documentData.setLastModUser(getUserData().getUser());
				EditDocumentPage page = new EditDocumentPage(DocumentsListPage.this, new Model<DocumentData>(documentData));
				setResponsePage(page);
			}
		};
		AjaxLink<Void> returnLink = new AjaxLink<Void>("return-link") {

			private static final long serialVersionUID = -601211439161207615L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(pageToReturn);
			}
		};
		add(createDocumentLink, returnLink, table);
	}

	private Settings prepareSettingsForTable() {
		Settings settings = new Settings();
		settings.setRowCount(5);
		settings.setIncludeButtons(true);
		settings.addItem("title", getString("table.doc.name"), 1, 3);
		settings.addItem("description",
				getString("table.doc.description"), 2, 5);
		settings.addItem("createUser.name",
				getString("table.doc.creator"), 3, 3);
		return settings;
	}
}

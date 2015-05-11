package com.livedoc.ui.documents;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.domain.entities.Project;
import com.livedoc.ui.pages.MasterPage;

public class DocumentsPage extends MasterPage {

	private static final long serialVersionUID = 1573553502836695372L;

	// models
	private Project project;
	private DocumentsCatalog documentsCatalog;

	// components
	private CategoriesListPanel categoriesPanel;
	private DocumentPanel documentPanel;

	public DocumentsPage(Project project) {
		super();
		this.project = project;
		documentsCatalog = new DocumentsCatalog();
		documentsCatalog.setCategories(project.getCategories());
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		categoriesPanel = new CategoriesListPanel("categories",
				new Model<DocumentsCatalog>(documentsCatalog)) {

			private static final long serialVersionUID = 7989896903291188341L;

			@Override
			public void onDocumentChoice(AjaxRequestTarget target) {
				target.add(documentPanel);
			}
		};
		add(categoriesPanel);

		documentPanel = new DocumentPanel("document",
				new PropertyModel<DocumentData>(documentsCatalog,
						"selectedDocument")) {
			private static final long serialVersionUID = 2572870193781782791L;

			@Override
			public void onConfigure() {
				super.onConfigure();
				setVisible(documentsCatalog.getSelectedDocument() != null);
			}
		};
		add(documentPanel);
		documentPanel.setOutputMarkupId(true);
		documentPanel.setOutputMarkupPlaceholderTag(true);

	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}

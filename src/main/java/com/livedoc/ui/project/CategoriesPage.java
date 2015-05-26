package com.livedoc.ui.project;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.services.CategoryService;
import com.livedoc.bl.services.DocumentService;
import com.livedoc.ui.pages.HomePage;
import com.livedoc.ui.pages.MasterPage;
import com.livedoc.ui.project.document.DocumentPanel;

public class CategoriesPage extends MasterPage {

	private static final long serialVersionUID = 1573553502836695372L;

	// services
	@SpringBean
	private CategoryService categoryService;
	@SpringBean
	private DocumentService documentService;

	// models
	private DocumentsCatalog documentsCatalog;

	// components
	private CategoriesListPanel categoriesPanel;
	private DocumentPanel documentPanel;

	public CategoriesPage(Project project) {
		super();
		documentsCatalog = new DocumentsCatalog();
		documentsCatalog.setCategories(categoryService
				.getNonEmptyProjectCategories(project));
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		// category list in the left part of page
		WebMarkupContainer categoriesContainer = new WebMarkupContainer(
				"categoriesContainer") {
			private static final long serialVersionUID = -2828009368733957009L;

			@Override
			public void onConfigure() {
				super.onConfigure();
				setVisible(!hideCategories());
			}
		};
		categoriesPanel = new CategoriesListPanel("categories",
				new PropertyModel<List<Category>>(documentsCatalog,
						"categories")) {
			private static final long serialVersionUID = 7989896903291188341L;

			@Override
			public void onDocumentChoice(AjaxRequestTarget target,
					DocumentData selectedDocument) {
				getDocumentsCatalog().setSelectedDocument(selectedDocument);
				getDocumentsCatalog().setChapters(
						documentService.getChapters(selectedDocument));
				documentPanel.resetPagination();
				target.add(documentPanel);
			}
		};
		categoriesContainer.add(categoriesPanel);
		add(categoriesContainer);

		// placeholder panel in the right part to show message when categories
		// list is empty
		WebMarkupContainer placeholder = new WebMarkupContainer("placeholder") {

			private static final long serialVersionUID = 5761846198040349412L;

			@Override
			public void onConfigure() {
				super.onConfigure();
				setVisible(hideCategories());
			}
		};
		AjaxLink<Void> backButton = new AjaxLink<Void>("backToHome") {

			private static final long serialVersionUID = -4362792643573755568L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(HomePage.class);
			}
		};
		placeholder.add(backButton);
		add(placeholder);

		documentPanel = new DocumentPanel("documentPanel",
				new Model<DocumentsCatalog>(documentsCatalog)) {

			private static final long serialVersionUID = 1398924416539409612L;

			@Override
			public void onConfigure() {
				super.onConfigure();
				setVisible(getDocumentsCatalog().getSelectedDocument() != null);
			}
		};
		documentPanel.setOutputMarkupId(true);
		documentPanel.setOutputMarkupPlaceholderTag(true);
		add(documentPanel);
	}

	protected DocumentsCatalog getDocumentsCatalog() {
		return documentsCatalog;
	}

	protected boolean hideCategories() {
		return CollectionUtils.isEmpty(documentsCatalog.getCategories());
	}
}

package com.livedoc.ui.documents;

import org.apache.commons.collections.CollectionUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.services.CategoryService;
import com.livedoc.ui.pages.HomePage;
import com.livedoc.ui.pages.MasterPage;

public class DocumentsPage extends MasterPage {

	private static final long serialVersionUID = 1573553502836695372L;

	// services
	@SpringBean
	private CategoryService categoryService;

	// models
	private Project project;
	private DocumentsCatalog documentsCatalog;

	// components
	private CategoriesListPanel categoriesPanel;
	private DocumentPanel documentPanel;
	private WebMarkupContainer documentContainer;

	private boolean hideCategories;

	public DocumentsPage(Project project) {
		super();
		this.project = project;
		documentsCatalog = new DocumentsCatalog();
		documentsCatalog.setCategories(categoryService
				.getNonEmptyProjectCategories(project));
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		hideCategories = CollectionUtils.isEmpty(documentsCatalog
				.getCategories());
		WebMarkupContainer categoriesContainer = new WebMarkupContainer(
				"categoriesContainer") {

			private static final long serialVersionUID = -2828009368733957009L;

			@Override
			public void onConfigure() {
				super.onConfigure();
				setVisible(!hideCategories);
			}
		};
		categoriesPanel = new CategoriesListPanel("categories",
				new Model<DocumentsCatalog>(documentsCatalog)) {

			private static final long serialVersionUID = 7989896903291188341L;

			@Override
			public void onDocumentChoice(AjaxRequestTarget target) {
				target.add(documentContainer);
			}

		};
		categoriesContainer.add(categoriesPanel);
		add(categoriesContainer);

		documentContainer = new WebMarkupContainer("documentContainer") {

			private static final long serialVersionUID = 2572870193781782791L;

			@Override
			public void onConfigure() {
				super.onConfigure();
				setVisible(documentsCatalog.getSelectedDocument() != null);
			}
		};
		documentPanel = new DocumentPanel("document",
				new PropertyModel<DocumentData>(documentsCatalog,
						"selectedDocument"));
		documentContainer.add(documentPanel);
		add(documentContainer);
		documentContainer.setOutputMarkupId(true);
		documentContainer.setOutputMarkupPlaceholderTag(true);

		WebMarkupContainer placeholder = new WebMarkupContainer("placeholder") {

			private static final long serialVersionUID = 5761846198040349412L;

			@Override
			public void onConfigure() {
				super.onConfigure();
				setVisible(hideCategories);
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

	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}

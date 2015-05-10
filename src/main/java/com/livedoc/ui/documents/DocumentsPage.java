package com.livedoc.ui.documents;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.domain.entities.Project;
import com.livedoc.ui.pages.MasterPage;

public class DocumentsPage extends MasterPage {

	private static final long serialVersionUID = 1573553502836695372L;

	// models
	private Project project;

	// components
	private ListView<Category> categoriesList;

	public DocumentsPage(Project project) {
		super();
		this.project = project;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		categoriesList = new ListView<Category>("categories-list",
				new PropertyModel<List<Category>>(this, "project.categories")) {

			private static final long serialVersionUID = 2514566140121578556L;

			@Override
			protected void populateItem(ListItem<Category> item) {
				final WebMarkupContainer collapsibleContainer = new WebMarkupContainer(
						"documents-container");
				collapsibleContainer.setOutputMarkupId(true);

				AjaxLink<Void> toggleLink = new AjaxLink<Void>("toggle-link") {
					private static final long serialVersionUID = -5351766568804326818L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// nothing to do
					}

					@Override
					public void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						tag.getAttributes().put("href",
								"#" + collapsibleContainer.getMarkupId());
					}
				};
				Label categoryName = new Label(
						"category-name",
						new PropertyModel<String>(item.getModelObject(), "name"));
				toggleLink.add(categoryName);
				item.add(toggleLink, collapsibleContainer);

				ListView<DocumentData> documentList = new ListView<DocumentData>(
						"documents", item.getModelObject()
								.getDocumentDataList()) {

					private static final long serialVersionUID = 5803604187641858874L;

					@Override
					protected void populateItem(
							final ListItem<DocumentData> item) {
						AjaxLink<Void> documentLink = new AjaxLink<Void>(
								"documentLink") {

							private static final long serialVersionUID = 7441037594661952970L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								// TODO
							}
						};
						Label documentName = new Label("document-name",
								new PropertyModel<String>(item.getModel(),
										"title"));
						documentLink.add(documentName);
						item.add(documentLink);
					}
				};
				collapsibleContainer.add(documentList);
			}
		};
		add(categoriesList);
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}

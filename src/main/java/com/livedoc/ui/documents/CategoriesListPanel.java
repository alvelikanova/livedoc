package com.livedoc.ui.documents;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;

public abstract class CategoriesListPanel extends
		GenericPanel<DocumentsCatalog> {

	private static final long serialVersionUID = -2205553620797652974L;

	// components
	private ListView<Category> categoriesList;

	private static final String CSS_EXPANDED = "glyphicon glyphicon-chevron-up pull-right";
	private static final String CSS_COLLAPSED = "glyphicon glyphicon-chevron-down pull-right";

	public CategoriesListPanel(String id, IModel<DocumentsCatalog> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		categoriesList = new ListView<Category>("categories-list",
				new PropertyModel<List<Category>>(getModel(), "categories")) {

			private static final long serialVersionUID = 2514566140121578556L;

			@Override
			protected void populateItem(ListItem<Category> item) {
				final CssModel cssModel = new CssModel();
				final WebMarkupContainer collapsibleContainer = new WebMarkupContainer(
						"documents-container");
				collapsibleContainer.setOutputMarkupId(true);

				final WebMarkupContainer expandIcon = new WebMarkupContainer(
						"expandIcon") {
					private static final long serialVersionUID = -5004078034930115862L;

					@Override
					public void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						tag.getAttributes().put("class", cssModel.getObject());
					}
				};
				expandIcon.setOutputMarkupId(true);

				AjaxLink<Void> toggleLink = new AjaxLink<Void>("toggle-link") {
					private static final long serialVersionUID = -5351766568804326818L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						target.add(expandIcon);
						cssModel.inverse();
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

				toggleLink.add(expandIcon);
				item.add(toggleLink, categoryName, collapsibleContainer);

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
								CategoriesListPanel.this.getModelObject()
										.setSelectedDocument(
												item.getModelObject());
								CategoriesListPanel.this
										.onDocumentChoice(target);
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

	public abstract void onDocumentChoice(AjaxRequestTarget target);

	class CssModel extends Model<String> {

		private static final long serialVersionUID = 7565266653380449721L;

		private boolean expanded = false;

		public String getObject() {
			return expanded ? CSS_EXPANDED : CSS_COLLAPSED;
		}

		public boolean isExpanded() {
			return expanded;
		}

		public void inverse() {
			this.expanded = !this.expanded;
		}

	}
}

package com.livedoc.ui.projects.categories;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.services.DocumentService;

public abstract class CategoriesListPanel extends GenericPanel<List<Category>> {

	private static final long serialVersionUID = -2205553620797652974L;

	private static final String ARROW_EXPANDED = "glyphicon glyphicon-chevron-up pull-right";
	private static final String ARROW_COLLAPSED = "glyphicon glyphicon-chevron-down pull-right";
	private static final String CONTAINER_EXPANDED = "panel-collapse collapsed collapse in";
	private static final String CONTAINER_COLLAPSED = "panel-collapse collapsed collapse";
	private static final String LINK_COLLAPSED = "collapsed";

	// services
	@SpringBean
	private DocumentService documentService;

	// components
	private RefreshingView<ExpandableCategory> categoriesList;

	// models
	private List<IModel<ExpandableCategory>> expandableCategories;

	public CategoriesListPanel(String id, IModel<List<Category>> model) {
		super(id, model);
		List<Category> categories = getModelObject();
		expandableCategories = new ArrayList<IModel<ExpandableCategory>>();
		for (Category category : categories) {
			ExpandableCategory expandableCategory = new ExpandableCategory();
			expandableCategory.setCategory(category);
			expandableCategory.setExpanded(false);
			expandableCategories.add(new Model<ExpandableCategory>(
					expandableCategory));
		}
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		categoriesList = new RefreshingView<ExpandableCategory>(
				"categories-list") {

			private static final long serialVersionUID = 2514566140121578556L;

			@Override
			protected Iterator<IModel<ExpandableCategory>> getItemModels() {
				return expandableCategories.iterator();
			}

			@Override
			protected void populateItem(final Item<ExpandableCategory> item) {
				final WebMarkupContainer collapsibleContainer = new WebMarkupContainer(
						"documents-container") {
					private static final long serialVersionUID = -3667563851918388954L;

					@Override
					public void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						tag.getAttributes()
								.put("class",
										item.getModelObject().isExpanded() ? CONTAINER_EXPANDED
												: CONTAINER_COLLAPSED);
						tag.getAttributes().put(
								"aria-expanded",
								item.getModelObject().isExpanded() ? "true"
										: "false");
						tag.getAttributes().put(
								"style",
								item.getModelObject().isExpanded() ? ""
										: "height: 0px;");
					}
				};
				collapsibleContainer.setOutputMarkupId(true);

				final WebMarkupContainer expandIcon = new WebMarkupContainer(
						"expandIcon") {
					private static final long serialVersionUID = -5004078034930115862L;

					@Override
					public void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						tag.getAttributes()
								.put("class",
										item.getModelObject().isExpanded() ? ARROW_EXPANDED
												: ARROW_COLLAPSED);
					}
				};
				expandIcon.setOutputMarkupId(true);

				AjaxLink<Void> toggleLink = new AjaxLink<Void>("toggle-link") {
					private static final long serialVersionUID = -5351766568804326818L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						target.add(expandIcon);
						boolean expanded = item.getModelObject().isExpanded();
						item.getModelObject().setExpanded(!expanded);
					}

					@Override
					public void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						tag.getAttributes().put("href",
								"#" + collapsibleContainer.getMarkupId());
						tag.getAttributes().put(
								"class",
								item.getModelObject().isExpanded() ? ""
										: LINK_COLLAPSED);
						tag.getAttributes().put(
								"aria-expanded",
								item.getModelObject().isExpanded() ? "true"
										: "false");
					}
				};
				Label categoryName = new Label("category-name",
						new PropertyModel<String>(item.getModelObject()
								.getCategory(), "name"));

				toggleLink.add(expandIcon);
				item.add(toggleLink, categoryName, collapsibleContainer);

				ListView<DocumentData> documentList = new ListView<DocumentData>(
						"documents", item.getModelObject().getCategory()
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
								CategoriesListPanel.this.onDocumentChoice(
										target, item.getModelObject());
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

	public abstract void onDocumentChoice(AjaxRequestTarget target,
			DocumentData selectedDocument);

	class ExpandableCategory implements Serializable {

		private static final long serialVersionUID = -3988986699690663767L;

		private Category category;
		private boolean expanded;

		public Category getCategory() {
			return category;
		}

		public void setCategory(Category category) {
			this.category = category;
		}

		public boolean isExpanded() {
			return expanded;
		}

		public void setExpanded(boolean expanded) {
			this.expanded = expanded;
		}
	}
}

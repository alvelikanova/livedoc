package com.livedoc.ui.administration.projects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.Project;
import com.livedoc.ui.components.StubAjaxUpdate;

public class EditCategoriesPanel extends GenericPanel<Project> {
	private static final long serialVersionUID = -6417182211636805438L;

	private RefreshingView<Category> categoryList;
	private WebMarkupContainer listContainer;

	public EditCategoriesPanel(String id, IModel<Project> projectModel) {
		super(id, projectModel);
		addEmptyCategory();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		listContainer = new WebMarkupContainer("categoriesContainer");
		categoryList = new RefreshingView<Category>("categories") {
			private static final long serialVersionUID = -829818236805595829L;

			@Override
			protected void populateItem(final Item<Category> item) {
				RequiredTextField<String> categoryName = new RequiredTextField<String>(
						"name", new PropertyModel<String>(
								item.getModelObject(), "name"));
				categoryName.add(new StubAjaxUpdate());
				TextArea<String> categoryDescription = new TextArea<String>(
						"description", new PropertyModel<String>(
								item.getModelObject(), "description"));
				categoryDescription.add(new StubAjaxUpdate());
				AjaxLink<Void> addCategory = new AjaxLink<Void>("add-category") {
					private static final long serialVersionUID = 2122420344011957059L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						addEmptyCategory();
						target.add(listContainer);
					}
				};
				AjaxLink<Void> deleteCategory = new AjaxLink<Void>(
						"delete-category") {
					private static final long serialVersionUID = -4163146590707568905L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						EditCategoriesPanel.this.getModelObject()
								.getCategories().remove(item.getModelObject());
						target.add(listContainer);
					}

					@Override
					public void onConfigure() {
						super.onConfigure();
						setVisible(EditCategoriesPanel.this.getModelObject()
								.getCategories().size() > 1);
					}
				};
				item.add(categoryName, categoryDescription, addCategory,
						deleteCategory);
			}

			@Override
			protected Iterator<IModel<Category>> getItemModels() {
				List<IModel<Category>> models = new ArrayList<IModel<Category>>();
				for (Category category : getModelObject().getCategories()) {
					models.add(new Model<Category>(category));
				}
				return models.iterator();
			}
		};
		listContainer.setOutputMarkupId(true);
		listContainer.add(categoryList);
		add(listContainer);
	}

	private void addEmptyCategory() {
		Category category = new Category();
		category.setProject(getModelObject());
		getModelObject().getCategories().add(category);
	}
}

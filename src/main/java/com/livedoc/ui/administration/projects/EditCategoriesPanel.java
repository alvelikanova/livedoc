package com.livedoc.ui.administration.projects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.services.CategoryService;
import com.livedoc.ui.common.components.dialogs.MessageDialogContent;
import com.livedoc.ui.common.components.dialogs.ModalDialog;
import com.livedoc.ui.common.helpers.StubAjaxUpdate;

public class EditCategoriesPanel extends GenericPanel<Project> {
	private static final long serialVersionUID = -6417182211636805438L;

	// services
	@SpringBean
	private CategoryService categoryService;

	// components
	private RefreshingView<Category> categoryList;
	private WebMarkupContainer listContainer;
	private ModalDialog dialog;

	public EditCategoriesPanel(String id, IModel<Project> projectModel) {
		super(id, projectModel);
		if (getModelObject().getCategories().isEmpty()) {
			addEmptyCategory();
		}
		Injector.get().inject(this);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		dialog = new ModalDialog("dialog");
		add(dialog);

		listContainer = new WebMarkupContainer("categoriesContainer");
		categoryList = new RefreshingView<Category>("categories") {
			private static final long serialVersionUID = -829818236805595829L;

			@Override
			protected void populateItem(final Item<Category> item) {
				RequiredTextField<String> categoryName = new RequiredTextField<String>(
						"name", new PropertyModel<String>(
								item.getModelObject(), "name"));
				categoryName.add(new StubAjaxUpdate());
				categoryName.add(StringValidator.maximumLength(64));
				TextArea<String> categoryDescription = new TextArea<String>(
						"description", new PropertyModel<String>(
								item.getModelObject(), "description"));
				categoryDescription.add(new StubAjaxUpdate());
				categoryDescription.add(StringValidator.maximumLength(256));
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
						final Category category = item.getModelObject();
						dialog.setTitle(getString("delete.dialog.title"));
						dialog.setContent(new MessageDialogContent(dialog
								.getContentId(), new ResourceModel(
								"delete.dialog.content"),
								MessageDialogContent.Buttons.OK,
								MessageDialogContent.Buttons.CANCEL) {

							private static final long serialVersionUID = -156937675362688835L;

							@Override
							protected void onConfirm(AjaxRequestTarget target) {
								EditCategoriesPanel.this.getModelObject()
										.getCategories().remove(category);
								target.add(listContainer);
								dialog.close(target);
							}

							@Override
							protected void onCancel(AjaxRequestTarget target) {
								dialog.close(target);
							}
						});
						dialog.show(target);
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

package com.livedoc.ui.administration.documents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.services.ProjectService;
import com.livedoc.ui.administration.AdministrationPage;

public class DocumentsManagementPage extends AdministrationPage {

	private static final long serialVersionUID = 5512650977534398494L;

	@SpringBean
	private ProjectService projectService;
	private RefreshingView<Project> projectList;

	@Override
	public void onInitialize() {
		super.onInitialize();
		setDocumentMenuLinkSelected();

		projectList = new RefreshingView<Project>("projects-list") {
			private static final long serialVersionUID = -8204515789661613116L;

			@Override
			protected Iterator<IModel<Project>> getItemModels() {
				List<Project> projects = projectService.findAllProjects();
				List<IModel<Project>> models = new ArrayList<IModel<Project>>();
				for (Project project : projects) {
					models.add(new Model<Project>(project));
				}
				return models.iterator();
			}

			@Override
			protected void populateItem(final Item<Project> item) {
				// collapsibleContainer is not visible until toggleLink is
				// pressed
				final WebMarkupContainer collapsibleContainer = new WebMarkupContainer(
						"categories-container");
				// this instruction leads to unique id generation
				collapsibleContainer.setOutputMarkupId(true);

				AjaxLink<Void> toggleLink = new AjaxLink<Void>("toggle-link") {
					private static final long serialVersionUID = -5351766568804326818L;

					@Override
					public void onClick(AjaxRequestTarget target) {

					}

					@Override
					public void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						tag.getAttributes().put("href",
								"#" + collapsibleContainer.getMarkupId());
					}
				};
				Label projectName = new Label(
						"project-name",
						new PropertyModel<String>(item.getModelObject(), "name"));
				toggleLink.add(projectName);

				item.add(toggleLink, collapsibleContainer);

				// collapsibleContainer content
				WebMarkupContainer projectDescriptionContainer = new WebMarkupContainer(
						"project-description-container") {
					
					private static final long serialVersionUID = 4593014138879483249L;

					@Override
					protected void onConfigure() {
						super.onConfigure();
						setVisible(!StringUtils.isBlank(item.getModelObject()
								.getDescription()));
					}
				};
				Label projectDescription = new Label("project-description",
						new PropertyModel<String>(item.getModelObject(),
								"description"));
				collapsibleContainer.add(projectDescriptionContainer);
				projectDescriptionContainer.add(projectDescription);
				ListView<Category> categoryList = new ListView<Category>(
						"categories", item.getModelObject().getCategories()) {
					private static final long serialVersionUID = -5817919602635654686L;

					@Override
					protected void populateItem(final ListItem<Category> item) {
						AjaxLink<Void> categoryLink = new AjaxLink<Void>(
								"categoryLink") {
							private static final long serialVersionUID = 3258593078260661348L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								// TODO
							}
						};
						Label categoryName = new Label("category-name",
								new PropertyModel<String>(item.getModel(),
										"name"));
						categoryLink.add(categoryName);
						item.add(categoryLink);
					}
				};
				collapsibleContainer.add(categoryList);
			}
		};
		add(projectList);
	}
}

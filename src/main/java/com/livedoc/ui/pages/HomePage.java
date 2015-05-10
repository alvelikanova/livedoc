package com.livedoc.ui.pages;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

import com.livedoc.bl.domain.entities.Project;
import com.livedoc.ui.documents.DocumentsPage;

public class HomePage extends MasterPage {

	private static final long serialVersionUID = -1682465878978520170L;

	private ListView<Project> projectList;

	@Override
	public void onInitialize() {
		super.onInitialize();

		projectList = new ListView<Project>("projects-list",
				new PropertyModel<List<Project>>(getUserData().getUser(),
						"projects")) {

			private static final long serialVersionUID = 9196566703497474508L;

			@Override
			protected void populateItem(final ListItem<Project> item) {
				Label projectName = new Label(
						"project-name",
						new PropertyModel<String>(item.getModelObject(), "name"));
				Label projectDescription = new Label("project-description",
						new PropertyModel<String>(item.getModelObject(),
								"description"));
				AjaxLink<Void> viewProject = new AjaxLink<Void>("viewProject") {

					private static final long serialVersionUID = -3681326439505863261L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						DocumentsPage page = new DocumentsPage(
								item.getModelObject());
						setResponsePage(page);
					}
				};
				item.add(projectName, projectDescription, viewProject);
			}

		};
		add(projectList);
	}
}

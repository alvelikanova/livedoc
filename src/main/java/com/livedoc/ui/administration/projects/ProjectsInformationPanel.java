package com.livedoc.ui.administration.projects;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import com.livedoc.bl.domain.entities.Project;

public class ProjectsInformationPanel extends Panel {

	private static final long serialVersionUID = 84766984704091816L;
	private WebMarkupContainer projectsContainer;

	public ProjectsInformationPanel(String id) {
		super(id);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		projectsContainer = new WebMarkupContainer("projects-container");

		projectsContainer.setOutputMarkupId(true);

		PropertyColumn<Project, String> projectNameColumn = new PropertyColumn<Project, String>(new ResourceModel(
				"table.projects.name"), "name");

		List<IColumn<Project, String>> columns = new ArrayList<IColumn<Project, String>>();
		columns.add(projectNameColumn);

		DefaultDataTable<Project, String> projectsTable = new DefaultDataTable<Project, String>("projects", columns,
				new ProjectsProvider(), 5);
		projectsContainer.add(projectsTable);
		add(projectsContainer);
		
		add(new EditProjectInformationPanel("edit-project-panel") {

			private static final long serialVersionUID = 1414120944482887691L;

			@Override
			protected void onSave(AjaxRequestTarget target) {
				target.add(projectsContainer);
			}
		});
	}
}

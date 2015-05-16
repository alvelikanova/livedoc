package com.livedoc.ui.administration.projects;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;

import com.livedoc.bl.domain.entities.Project;
import com.livedoc.ui.administration.AdministrationPage;
import com.livedoc.ui.common.components.table.Settings;
import com.livedoc.ui.common.components.table.Table;

public class ProjectsManagementPage extends AdministrationPage {

	private static final long serialVersionUID = 6366226078694121949L;

	@Override
	public void onInitialize() {
		super.onInitialize();

		AjaxLink<Void> createProjectLink = new AjaxLink<Void>("createProject") {
			private static final long serialVersionUID = -6420561414459574501L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				EditProjectPage page = new EditProjectPage(
						ProjectsManagementPage.this);
				setResponsePage(page);
			}
		};
		add(createProjectLink);
		Settings settings = new Settings();
		settings.setRowCount(5);
		settings.setIncludeButtons(true);
		settings.addItem("name", getString("table.projects.name"), 1);
		settings.addItem("description",
				getString("table.projects.description"), 2);
		Table<Project, String> table = new Table<Project, String>(
				"projects-table", settings, new ProjectsProvider());
		table.setCellFunctionsProvider(new ProjectCellFunctionsProvider(
				ProjectsManagementPage.this, table));
		add(table);
	}

}

package com.livedoc.ui.administration.projects;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.services.ProjectService;
import com.livedoc.ui.common.components.table.CellFunctionsProvider;
import com.livedoc.ui.common.components.table.Table;

public class ProjectCellFunctionsProvider extends
		CellFunctionsProvider<Project, String> {

	private static final long serialVersionUID = 2481982216204914478L;

	@SpringBean
	private ProjectService projectService;
	private Page pageToReturn;

	public ProjectCellFunctionsProvider(Page pageToReturn,
			Table<Project, String> table) {
		super(table);
		this.pageToReturn = pageToReturn;
	}

	public Page edit(IModel<Project> model) {
		EditProjectPage page = new EditProjectPage(pageToReturn, model);
		return page;
	}

	public void delete(IModel<Project> model) {
		projectService.deleteProject(model.getObject());
		RequestCycle.get().find(AjaxRequestTarget.class).add(getTable());
	}

}

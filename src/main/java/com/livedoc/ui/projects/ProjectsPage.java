package com.livedoc.ui.projects;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

import com.livedoc.bl.domain.entities.Project;
import com.livedoc.ui.pages.HomePage;
import com.livedoc.ui.projects.categories.CategoriesPage;
import com.livedoc.ui.search.SearchResultsPage;

public class ProjectsPage extends HomePage {

    private static final long serialVersionUID = 3579237837046040928L;

    private ListView<Project> projectList;
    private List<Project> projects = new ArrayList<Project>();

    @Override
    public void onInitialize() {
	super.onInitialize();
	projects = getUserData().getUser().getProjects();
	projectList = new ListView<Project>("projects-list", projects) {

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
			CategoriesPage page = new CategoriesPage(
				item.getModelObject());
			setResponsePage(page);
		    }
		};
		item.add(projectName, projectDescription, viewProject);
	    }

	};
	add(projectList);
    }

    @Override
    protected void onSearch(AjaxRequestTarget target, String query) {
	SearchResultsPage page = new SearchResultsPage(query);
	setResponsePage(page);
    }
}

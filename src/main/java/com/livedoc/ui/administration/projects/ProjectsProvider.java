package com.livedoc.ui.administration.projects;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.services.ProjectService;

public class ProjectsProvider extends SortableDataProvider<Project, String> {

	private static final long serialVersionUID = 8544177816887433941L;
	
	@SpringBean
	private ProjectService projectService;

	public ProjectsProvider() {
		super();
		Injector.get().inject(this);
	}

	protected List<Project> getData() {
		return projectService.findAllProjects();
	}

	public Iterator<? extends Project> iterator(long first, long count) {
		List<Project> list = getData();
		long toIndex = first + count;
		if (toIndex > list.size()) {
			toIndex = list.size();
		}
		return list.subList((int) first, (int) toIndex).listIterator();
	}

	public long size() {
		return getData().size();
	}

	public IModel<Project> model(Project object) {
		return new Model<Project>(object);
	}
}

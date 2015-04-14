package com.livedoc.ui.administration;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;

import com.livedoc.ui.administration.documents.DocumentsManagementPage;
import com.livedoc.ui.administration.projects.ProjectsManagementPage;
import com.livedoc.ui.administration.users.UsersManagementPage;

public class AdministrationMenuPanel extends Panel {

	private static final long serialVersionUID = -6722656799781921547L;

	private MenuLink usersMenuLink;
	private MenuLink projectsMenuLink;
	private MenuLink documentMenuLink;

	public AdministrationMenuPanel(String id) {
		super(id);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		usersMenuLink = new MenuLink("users", UsersManagementPage.class);
		projectsMenuLink = new MenuLink("projects", ProjectsManagementPage.class);
		documentMenuLink = new MenuLink("documents", DocumentsManagementPage.class);
		add(usersMenuLink, projectsMenuLink, documentMenuLink);
	}

	private class MenuLink extends AjaxLink<Void> {

		private static final long serialVersionUID = -5715886500662880014L;
		private Class<? extends Page> responsePage;

		public MenuLink(String id, Class<? extends Page> responsePage) {
			super(id);
			this.responsePage = responsePage;
		}

		@Override
		public void onClick(AjaxRequestTarget target) {
			setResponsePage(responsePage);
		}
	}

}
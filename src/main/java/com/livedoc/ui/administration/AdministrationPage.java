package com.livedoc.ui.administration;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.services.UserService;
import com.livedoc.ui.administration.documents.DocumentsManagementPage;
import com.livedoc.ui.administration.projects.ProjectsManagementPage;
import com.livedoc.ui.administration.users.UsersManagementPage;
import com.livedoc.ui.pages.MasterPage;

public class AdministrationPage extends MasterPage {

	private static final long serialVersionUID = 3341079353601186137L;

	@SpringBean
	private UserService userService;
	private MenuLink usersMenuLink;
	private MenuLink projectsMenuLink;
	private MenuLink documentMenuLink;

	protected void setUsersMenuLinkSelected() {
		usersMenuLink.setSelected(true);
	}

	protected void setProjectsMenuLinkSelected() {
		projectsMenuLink.setSelected(false);
	}

	protected void setDocumentMenuLinkSelected() {
		documentMenuLink.setSelected(false);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		usersMenuLink = new MenuLink("users", UsersManagementPage.class);
		projectsMenuLink = new MenuLink("projects",
				ProjectsManagementPage.class);
		documentMenuLink = new MenuLink("documents",
				DocumentsManagementPage.class);
		add(usersMenuLink, projectsMenuLink, documentMenuLink);
	}

	class MenuLink extends AjaxLink<Void> {

		private static final long serialVersionUID = -5715886500662880014L;
		private Class<? extends Page> responsePage;
		private boolean selected = false;

		public MenuLink(String id, Class<? extends Page> responsePage) {
			super(id);
			this.responsePage = responsePage;
		}

		@Override
		public void onClick(AjaxRequestTarget target) {
			setResponsePage(responsePage);
		}

		@Override
		public void onComponentTag(ComponentTag tag) {
			super.onComponentTag(tag);
			if (selected) {
				tag.getAttributes().put("class", "active");
			} else {
				tag.getAttributes().put("class", "");
			}
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}
}

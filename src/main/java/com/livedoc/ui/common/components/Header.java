package com.livedoc.ui.common.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

import com.livedoc.security.SecurityUserDetails;
import com.livedoc.ui.administration.users.UsersManagementPage;
import com.livedoc.ui.pages.HomePage;
import com.livedoc.ui.pages.LoginPage;
import com.livedoc.ui.profile.UserProfilePage;

public class Header extends GenericPanel<SecurityUserDetails> {
	private static final long serialVersionUID = 891726251828753954L;

	public Header(String id, IModel<SecurityUserDetails> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		addLogoutLink();
		addAdministrationPageLink();
		addProfileLink();
		addHomeLink();
	}

	// links
	private void addLogoutLink() {
		AjaxLink<Void> logoutLink = new AjaxLink<Void>("logout") {
			private static final long serialVersionUID = 2473429138180754076L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				AuthenticatedWebSession session = AuthenticatedWebSession.get();
				session.signOut();
				session.invalidate();
				setResponsePage(LoginPage.class);
			}
		};
		add(logoutLink);
	}

	private void addAdministrationPageLink() {
		AjaxLink<Void> adminLink = new AjaxLink<Void>("admin") {
			private static final long serialVersionUID = 2473429138180754076L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(UsersManagementPage.class);
			}

			@Override
			public void onConfigure() {
				super.onConfigure();
				SecurityUserDetails details = Header.this.getModelObject();
				setVisible(details.isUserAdmin());
			}
		};
		add(adminLink);
	}

	private void addProfileLink() {
		AjaxLink<Void> profileLink = new AjaxLink<Void>("profile") {
			private static final long serialVersionUID = 2473429138180754076L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(UserProfilePage.class);
			}
		};
		add(profileLink);
	}

	private void addHomeLink() {
		AjaxLink<Void> homeLink = new AjaxLink<Void>("home") {
			private static final long serialVersionUID = 65530189341266805L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(HomePage.class);
			}
		};
		add(homeLink);
	}
}

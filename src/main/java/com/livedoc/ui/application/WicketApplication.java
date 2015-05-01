package com.livedoc.ui.application;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import com.livedoc.security.SpringWicketWebSession;
import com.livedoc.ui.administration.AdministrationPage;
import com.livedoc.ui.pages.HomePage;
import com.livedoc.ui.pages.LoginPage;

public class WicketApplication extends AuthenticatedWebApplication {

	@Override
	public Class<HomePage> getHomePage() {
		return HomePage.class;
	}

	public void init() {
		super.init();
		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this));
		mountPage("login", LoginPage.class);
		mountPage("admin", AdministrationPage.class);
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return LoginPage.class;
	}

	@Override
	protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
		return SpringWicketWebSession.class;
	}
}

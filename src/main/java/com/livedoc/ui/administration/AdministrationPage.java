package com.livedoc.ui.administration;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.services.UserService;
import com.livedoc.ui.pages.MasterPage;

public class AdministrationPage extends MasterPage {

	private static final long serialVersionUID = 3341079353601186137L;

	private AdministrationMenuPanel menuContainer;
	@SpringBean
	private UserService userService;

	@Override
	public void onInitialize() {
		super.onInitialize();
		menuContainer = new AdministrationMenuPanel("menu-panel");
		menuContainer.setOutputMarkupId(true);
		add(menuContainer);
	}

}

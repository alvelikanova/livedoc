package com.livedoc.ui.profile;

import org.apache.wicket.markup.html.basic.Label;

import com.livedoc.ui.pages.MasterPage;

public class UserProfilePage extends MasterPage {
	private static final long serialVersionUID = 8443776682992525320L;
	
	public UserProfilePage()
	{
		super();
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		Label label = new Label("user-login", getUserData().getUsername());
		add(label);
	}

}

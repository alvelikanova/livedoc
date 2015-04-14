package com.livedoc.ui.administration.users;

import com.livedoc.bl.domain.entities.User;
import com.livedoc.ui.administration.AdministrationPage;
import com.livedoc.ui.common.components.table.Settings;
import com.livedoc.ui.common.components.table.Table;

public class UsersManagementPage extends AdministrationPage {

	private static final long serialVersionUID = -5700112539037765027L;

	@Override
	public void onInitialize()
	{
		super.onInitialize();
		
		Settings settings = new Settings();
		settings.setRowCount(5);
		settings.getProperties().put("name", getString("table.users.name"));
		settings.getProperties().put("role.name", getString("table.users.role"));
		Table<User, String> table = new Table<User, String>("users-table", settings, new UsersProvider());
		add(table);
	}
}

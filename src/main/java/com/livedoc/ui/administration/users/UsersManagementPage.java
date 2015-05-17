package com.livedoc.ui.administration.users;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;

import com.livedoc.bl.domain.entities.User;
import com.livedoc.ui.administration.AdministrationPage;
import com.livedoc.ui.common.components.table.Settings;
import com.livedoc.ui.common.components.table.Table;

public class UsersManagementPage extends AdministrationPage {

	private static final long serialVersionUID = -5700112539037765027L;

	private Table<User, String> table;

	@Override
	public void onInitialize() {
		super.onInitialize();

		AjaxLink<Void> createUserLink = new AjaxLink<Void>("createUser") {
			private static final long serialVersionUID = -6420561414459574501L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				EditUserPage page = new EditUserPage(UsersManagementPage.this);
				setResponsePage(page);
			}
		};
		add(createUserLink);
		Settings settings = new Settings();
		settings.setRowCount(5);
		settings.setIncludeButtons(true);
		settings.addItem("name", getString("table.users.name"), 1, 3);
		settings.addItem("role.name", getString("table.users.role"), 2, 8);
		table = new Table<User, String>("users-table", settings,
				new UsersProvider());
		table.setCellFunctionsProvider(new UserCellFunctionsProvider(
				UsersManagementPage.this, table));
		add(table);
	}
}

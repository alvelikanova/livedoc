package com.livedoc.ui.administration.users;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.domain.entities.User;
import com.livedoc.bl.services.UserService;
import com.livedoc.ui.common.components.table.CellFunctionsProvider;
import com.livedoc.ui.common.components.table.Table;

public class UserCellFunctionsProvider extends
		CellFunctionsProvider<User, String> {

	private static final long serialVersionUID = -4580744225283266930L;

	@SpringBean
	private UserService userService;

	private Page pageToReturn;

	public UserCellFunctionsProvider(Page pageToReturn,
			Table<User, String> table) {
		super(table);
		this.pageToReturn = pageToReturn;
	}

	public Page edit(IModel<User> model) {
		EditUserPage page = new EditUserPage(pageToReturn, model);
		return page;
	}

	public void delete(IModel<User> model) {
		userService.deleteUser(model.getObject());
		RequestCycle.get().find(AjaxRequestTarget.class).add(getTable());
	}

}

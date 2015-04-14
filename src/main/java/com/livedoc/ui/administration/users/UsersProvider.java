package com.livedoc.ui.administration.users;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.domain.entities.User;
import com.livedoc.bl.services.UserService;

public class UsersProvider extends SortableDataProvider<User, String> {

	private static final long serialVersionUID = -8509098631265480066L;

	@SpringBean
	private UserService userService;

	public UsersProvider() {
		super();
		Injector.get().inject(this);
	}

	protected List<User> getData() {
		return userService.findAllUsers();
	}

	public Iterator<? extends User> iterator(long first, long count) {
		List<User> list = getData();
		long toIndex = first + count;
		if (toIndex > list.size()) {
			toIndex = list.size();
		}
		return list.subList((int) first, (int) toIndex).listIterator();
	}

	public long size() {
		return getData().size();
	}

	public IModel<User> model(User object) {
		return new Model<User>(object);
	}
}

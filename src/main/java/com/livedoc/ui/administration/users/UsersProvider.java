package com.livedoc.ui.administration.users;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.context.SecurityContextHolder;

import com.livedoc.bl.domain.entities.User;
import com.livedoc.bl.services.UserService;
import com.livedoc.security.SecurityUserDetails;

public class UsersProvider extends SortableDataProvider<User, String> {

	private static final long serialVersionUID = -8509098631265480066L;

	@SpringBean
	private UserService userService;

	public UsersProvider() {
		super();
		Injector.get().inject(this);
	}

	protected List<User> getData() {
		SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		return userService.findUsers(Arrays.asList(userDetails.getUser()
				.getId()));
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

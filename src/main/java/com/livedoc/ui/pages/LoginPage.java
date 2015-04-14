package com.livedoc.ui.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.PropertyModel;

import com.livedoc.bl.domain.entities.User;
import com.livedoc.ui.components.Feedback;

public class LoginPage extends WebPage {

	private static final long serialVersionUID = -8182667027794175022L;
	private static final Logger logger = Logger.getLogger(LoginPage.class);
	private User user = new User();

	@Override
	public void onInitialize() {
		super.onInitialize();
		add(new LoginForm("form"));
	}

	private class LoginForm extends Form<Void> {

		private static final long serialVersionUID = 6675882838123700867L;

		public LoginForm(String id) {
			super(id);
		}

		@Override
		public void onInitialize() {
			super.onInitialize();
			add(new Feedback("feedback"));
			RequiredTextField<String> usernameTextField = new RequiredTextField<String>(
					"username", new PropertyModel<String>(user, "name"));
			PasswordTextField passwordTextField = new PasswordTextField(
					"password",
					new PropertyModel<String>(user, "password"));
			add(usernameTextField, passwordTextField);

		}

		@Override
		public void onSubmit() {
			AuthenticatedWebSession session = AuthenticatedWebSession.get();
			if (session.signIn(user.getName(), user.getPassword())) {
				logger.info(String.format("User %s logged in", user.getName()));
				setResponsePage(getApplication().getHomePage());
			} else {
				error(getString("login.failed"));
			}
		}
	}
}

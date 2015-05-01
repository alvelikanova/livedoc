package com.livedoc.ui.profile;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Objects;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.livedoc.bl.domain.entities.User;
import com.livedoc.bl.services.UserService;
import com.livedoc.ui.common.components.Feedback;
import com.livedoc.ui.pages.MasterPage;

public class UserProfilePage extends MasterPage {
	private static final long serialVersionUID = 8443776682992525320L;
	private Form<User> passwordChangeForm;
	private PasswordTextField oldPassword;
	private PasswordTextField newPassword;
	private PasswordTextField repeatNewPassword;
	private Feedback feedbackPanel;
	private WebMarkupContainer collapsibleContainer;
	@SpringBean
	private UserService userService;
	@SpringBean
	private BCryptPasswordEncoder passwordEncoder;
	private String oldPasswordStub;

	public UserProfilePage() {
		super();
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		// User profile: username
		Label label = new Label("title", String.format("%s: %s",
				getString("profile.title"), getUserData().getUsername()));
		add(label);

		collapsibleContainer = new WebMarkupContainer(
				"password-change-container");
		collapsibleContainer.setOutputMarkupId(true);
		collapsibleContainer.setOutputMarkupPlaceholderTag(true);

		AjaxLink<Void> toggleLink = new AjaxLink<Void>("toggle-link") {
			private static final long serialVersionUID = -5351766568804326818L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				target.add(passwordChangeForm);
			}

			@Override
			public void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.getAttributes().put("href",
						"#" + collapsibleContainer.getMarkupId());
			}
		};
		add(collapsibleContainer, toggleLink);

		passwordChangeForm = new Form<User>("password-change-form");
		passwordChangeForm.setOutputMarkupId(true);
		collapsibleContainer.add(passwordChangeForm);

		feedbackPanel = new Feedback("feedback");
		feedbackPanel.setOutputMarkupId(true);
		passwordChangeForm.add(feedbackPanel);

		// Password change
		oldPassword = new PasswordTextField("old-password", new Model<String>(
				oldPasswordStub));
		oldPassword.setRequired(true);
		newPassword = new PasswordTextField("new-password",
				new PropertyModel<String>(getUserData().getUser(), "password"));
		newPassword.setRequired(true);
		repeatNewPassword = new PasswordTextField("repeat-new-password",
				new PropertyModel<String>(getUserData().getUser(), "password"));
		repeatNewPassword.setRequired(true);

		AjaxButton savePasswords = new AjaxButton("change-password",
				passwordChangeForm) {

			private static final long serialVersionUID = 7058668736660619349L;

			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				User user = getUserData().getUser();
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				userService.saveUser(user);
				collapseContainer(target);
			}

			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}
		};

		AjaxLink<Void> cancelChanging = new AjaxLink<Void>(
				"cancel-change-password") {

			private static final long serialVersionUID = 4068459105924323291L;

			@Override
			public void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.getAttributes().put("href",
						"#" + collapsibleContainer.getMarkupId());
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				// nothing to do
			}
		};

		passwordChangeForm.add(oldPassword, newPassword, repeatNewPassword,
				savePasswords, cancelChanging);
		passwordChangeForm.add(new ChangePasswordValidator());
	}

	private void collapseContainer(AjaxRequestTarget target) {
		target.appendJavaScript("$('#" + collapsibleContainer.getMarkupId()
				+ "').collapse('hide')");
	}

	class ChangePasswordValidator implements IFormValidator {

		private static final long serialVersionUID = 5634056633651826952L;

		public FormComponent<?>[] getDependentFormComponents() {
			return new FormComponent<?>[] { oldPassword, newPassword,
					repeatNewPassword };
		}

		public void validate(Form<?> form) {
			if (!Objects.equal(newPassword.getInput(),
					repeatNewPassword.getInput())) {
				repeatNewPassword.error(getString("passwordsNotEqual"));
			}
			if (!passwordEncoder.matches(oldPassword.getInput(), getUserData()
					.getPassword())) {
				oldPassword.error(getString("wrongOldPassword"));
			}
		}
	}
}

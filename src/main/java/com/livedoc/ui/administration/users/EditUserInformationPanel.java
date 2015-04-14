package com.livedoc.ui.administration.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.wicketstuff.select2.Response;
import org.wicketstuff.select2.Select2MultiChoice;
import org.wicketstuff.select2.TextChoiceProvider;

import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.domain.entities.Role;
import com.livedoc.bl.domain.entities.User;
import com.livedoc.bl.services.ProjectService;
import com.livedoc.bl.services.RoleService;
import com.livedoc.bl.services.UserService;
import com.livedoc.ui.common.helpers.DomainEntityChoiceRenderer;
import com.livedoc.ui.components.Feedback;

public abstract class EditUserInformationPanel extends GenericPanel<User> {

	private static final long serialVersionUID = 3950401087826313151L;

	@SpringBean
	private RoleService roleService;
	@SpringBean
	private ProjectService projectService;
	@SpringBean
	private UserService userService;
	@SpringBean
	private BCryptPasswordEncoder passwordEncoder;

	private Feedback feedbackPanel;

	public EditUserInformationPanel(String id) {
		super(id, new Model<User>(new User()));
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		final Form<Void> form = new Form<Void>("form");
		form.setOutputMarkupId(true);
		add(form);

		feedbackPanel = new Feedback("feedback");
		feedbackPanel.setOutputMarkupId(true);
		form.add(feedbackPanel);

		RequiredTextField<String> usernameField = new RequiredTextField<String>(
				"username-field", new PropertyModel<String>(getModel(), "name"));

		PasswordTextField passwordField = new PasswordTextField(
				"password-field", new PropertyModel<String>(getModel(),
						"password"));

		PasswordTextField repeatPasswordField = new PasswordTextField(
				"repeat-password-field", new PropertyModel<String>(getModel(),
						"password"));

		EqualPasswordInputValidator passwordValidator = new EqualPasswordInputValidator(
				passwordField, repeatPasswordField);
		form.add(passwordValidator);

		List<Role> roles = roleService.findAllRoles();
		DropDownChoice<Role> rolesDropDownChoice = new DropDownChoice<Role>(
				"roles-choices", new PropertyModel<Role>(getModel(), "role"),
				roles, new DomainEntityChoiceRenderer<Role>() {

					private static final long serialVersionUID = 8719030878640527743L;

					public Object getDisplayValue(Role object) {
						return object.getName();
					}
				});
		rolesDropDownChoice.setRequired(true);

		Select2MultiChoice<Project> projectsSelector = new Select2MultiChoice<Project>(
				"projects", new PropertyModel<Collection<Project>>(getModel(),
						"projects"), new ProjectsProvider());

		form.add(usernameField, passwordField, repeatPasswordField,
				rolesDropDownChoice, projectsSelector);

		AjaxButton saveButton = new AjaxButton("save-button", form) {

			private static final long serialVersionUID = 131954482957027000L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				User user = EditUserInformationPanel.this.getModelObject();

				// 1. check if user already exist
				if (userService.findUserByLoginName(user.getName()) != null) {
					error(getString("saving.alreadyexists"));
					target.add(feedbackPanel);
					return;
				}

				// 2. set encrypted password
				user.setPassword(passwordEncoder.encode(user.getPassword()));

				// 3. save user
				userService.saveUser(user);

				// 4. tell Wicket to show message
				EditUserInformationPanel.this
						.success(getString("saving.success"));

				// 5. reset model
				EditUserInformationPanel.this.setModelObject(new User());

				// 6. refresh form and table with users
				target.add(form);
				EditUserInformationPanel.this.onSave(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}
		};

		AjaxLink<Void> cancelButton = new AjaxLink<Void>("cancel-button") {

			private static final long serialVersionUID = 3159133009240454583L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				EditUserInformationPanel.this.setModelObject(new User());
				target.add(form);
			}
		};
		form.add(saveButton, cancelButton);

	}

	/**
	 * Need to refresh table with users
	 * 
	 * @param target
	 */
	protected abstract void onSave(AjaxRequestTarget target);

	private class ProjectsProvider extends TextChoiceProvider<Project> {

		private static final long serialVersionUID = 6600938023093463927L;

		@Override
		protected String getDisplayText(Project choice) {
			return choice.getName();
		}

		@Override
		protected Object getId(Project choice) {
			return choice.getId();
		}

		@Override
		public void query(String term, int page, Response<Project> response) {
			response.addAll(projectService.findAllProjects());
			response.setHasMore(response.size() == 10);
		}

		@Override
		public Collection<Project> toChoices(Collection<String> ids) {
			ArrayList<Project> projects = new ArrayList<Project>();
			for (String id : ids) {
				projects.add(projectService.findProjectById(id));
			}
			return projects;
		}
	}

}

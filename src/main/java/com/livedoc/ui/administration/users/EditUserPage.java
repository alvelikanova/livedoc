package com.livedoc.ui.administration.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
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
import com.livedoc.ui.pages.MasterPage;

public class EditUserPage extends MasterPage {

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
	private Form<User> form;
	private Page pageToReturn;

	public EditUserPage(Page pageToReturn) {
		super();
		this.pageToReturn = pageToReturn;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		form = new Form<User>("form", new Model<User>(new User()));
		form.setOutputMarkupId(true);
		add(form);

		feedbackPanel = new Feedback("feedback");
		feedbackPanel.setOutputMarkupId(true);
		form.add(feedbackPanel);

		RequiredTextField<String> usernameField = new RequiredTextField<String>(
				"username", new PropertyModel<String>(form.getModel(),
						"name"));

		PasswordTextField passwordField = new PasswordTextField(
				"password", new PropertyModel<String>(form.getModel(),
						"password"));

		PasswordTextField repeatPasswordField = new PasswordTextField(
				"repeat-password", new PropertyModel<String>(
						form.getModel(), "password"));

		EqualPasswordInputValidator passwordValidator = new EqualPasswordInputValidator(
				passwordField, repeatPasswordField);
		form.add(passwordValidator);

		List<Role> roles = roleService.findAllRoles();
		DropDownChoice<Role> rolesDropDownChoice = new DropDownChoice<Role>(
				"roles-choices", new PropertyModel<Role>(form.getModel(),
						"role"), roles, new DomainEntityChoiceRenderer<Role>() {

					private static final long serialVersionUID = 8719030878640527743L;

					public Object getDisplayValue(Role object) {
						return object.getName();
					}
				});
		rolesDropDownChoice.setRequired(true);

		Select2MultiChoice<Project> projectsSelector = new Select2MultiChoice<Project>(
				"projects", new PropertyModel<Collection<Project>>(
						form.getModel(), "projects"), new ProjectsProvider());
		
		form.add(usernameField, passwordField, repeatPasswordField,
				rolesDropDownChoice, projectsSelector);

		AjaxButton saveButton = new AjaxButton("save-button", form) {

			private static final long serialVersionUID = 131954482957027000L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				User user = (User) form.getModelObject();
				if (userService.findUserByLoginName(user.getName()) != null) {
					error(getString("saving.alreadyexists"));
					target.add(feedbackPanel);
					return;
				}
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				userService.saveUser(user);
				setResponsePage(pageToReturn);
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
				// TODO confirmation dialog?
				setResponsePage(pageToReturn);
			}
		};
		form.add(saveButton, cancelButton);
	}

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
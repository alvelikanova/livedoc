package com.livedoc.ui.administration.projects;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.services.ProjectService;
import com.livedoc.ui.components.Feedback;

public abstract class EditProjectInformationPanel extends GenericPanel<Project> {

	private static final long serialVersionUID = 6391339627405361984L;

	private Feedback feedbackPanel;

	@SpringBean
	private ProjectService projectService;

	public EditProjectInformationPanel(String id) {
		super(id, new Model<Project>(new Project()));
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

		TextField<String> projectNameField = new TextField<String>(
				"project-name", new PropertyModel<String>(getModel(),
						"name"));
		projectNameField.add(StringValidator.maximumLength(32));
		projectNameField.setRequired(true);

		TextArea<String> projectDescriptionField = new TextArea<String>(
				"project-description", new PropertyModel<String>(
						getModel(), "description"));
		projectDescriptionField.add(StringValidator.maximumLength(256));

		AjaxButton saveButton = new AjaxButton("save-button", form) {

			private static final long serialVersionUID = 131954482957027000L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Project project = EditProjectInformationPanel.this
						.getModelObject();
				projectService.saveProject(project);
				EditProjectInformationPanel.this
						.success(getString("saving.success"));
				EditProjectInformationPanel.this.setModelObject(new Project());
				target.add(form);
				EditProjectInformationPanel.this.onSave(target);
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
				EditProjectInformationPanel.this.setModelObject(new Project());
				target.add(form);
			}
		};
		form.add(saveButton, cancelButton);

		form.add(projectNameField, projectDescriptionField);
	}

	protected abstract void onSave(AjaxRequestTarget target);

}

package com.livedoc.ui.administration.projects;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.services.ProjectService;
import com.livedoc.ui.components.Feedback;
import com.livedoc.ui.pages.MasterPage;

public class EditProjectPage extends MasterPage {

	private static final long serialVersionUID = 6391339627405361984L;

	@SpringBean
	private ProjectService projectService;
	
	private Feedback feedbackPanel;
	private Page pageToReturn;
	private Form<Project> form;

	public EditProjectPage(Page pageToReturn) {
		super();
		this.pageToReturn = pageToReturn;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		form = new Form<Project>("form", new Model<Project>(new Project()));
		form.setOutputMarkupId(true);
		add(form);

		feedbackPanel = new Feedback("feedback");
		feedbackPanel.setOutputMarkupId(true);
		form.add(feedbackPanel);

		TextField<String> projectNameField = new TextField<String>(
				"project-name", new PropertyModel<String>(form.getModel(),
						"name"));
		projectNameField.add(StringValidator.maximumLength(32));
		projectNameField.setRequired(true);

		TextArea<String> projectDescriptionField = new TextArea<String>(
				"project-description", new PropertyModel<String>(
						form.getModel(), "description"));
		projectDescriptionField.add(StringValidator.maximumLength(256));

		form.add(new EditCategoriesPanel("categories", form.getModel()));
		
		AjaxButton saveButton = new AjaxButton("save-button", form) {

			private static final long serialVersionUID = 131954482957027000L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Project project = (Project) form.getModelObject();
				projectService.saveProject(project);
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
				//TODO confirmation dialog
				setResponsePage(pageToReturn);
			}
		};
		form.add(saveButton, cancelButton);
		form.add(projectNameField, projectDescriptionField);
	}
}

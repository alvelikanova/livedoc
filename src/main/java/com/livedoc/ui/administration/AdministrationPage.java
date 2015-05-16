package com.livedoc.ui.administration;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.services.UserService;
import com.livedoc.ui.administration.documents.DocumentsManagementPage;
import com.livedoc.ui.administration.projects.ProjectsManagementPage;
import com.livedoc.ui.administration.users.UsersManagementPage;
import com.livedoc.ui.pages.MasterPage;

public class AdministrationPage extends MasterPage {

	private static final long serialVersionUID = 3341079353601186137L;

	private static final String CSS_ACTIVE = "active";

	@SuppressWarnings("unchecked")
	private static final List<Class<? extends AdministrationPage>> adminPages = Arrays
			.asList(UsersManagementPage.class, ProjectsManagementPage.class,
					DocumentsManagementPage.class);

	@SpringBean
	private UserService userService;

	@Override
	public void onInitialize() {
		super.onInitialize();
		ListView<Class<? extends AdministrationPage>> menu = new ListView<Class<? extends AdministrationPage>>(
				"menu", adminPages) {
			private static final long serialVersionUID = 4356129357585260114L;

			@Override
			protected void populateItem(
					final ListItem<Class<? extends AdministrationPage>> item) {
				AjaxLink<Void> link = new AjaxLink<Void>("link") {
					private static final long serialVersionUID = 4584533839750057685L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						setResponsePage(item.getModelObject());
					}
				};
				Label label = new Label("label", new ResourceModel(item
						.getModelObject().getSimpleName() + ".link"));
				link.add(label);
				item.add(link);
				item.add(new AttributeModifier("class", new Model<String>() {
					private static final long serialVersionUID = 6117884528348055760L;

					public String getObject() {
						return getPage().getClass().equals(
								item.getModelObject()) ? CSS_ACTIVE : "";
					}
				}));
			}
		};
		add(menu);
	}
}

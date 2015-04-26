package com.livedoc.ui.common.components;

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class MessageDialogContent extends Panel {

	private static final long serialVersionUID = -4122580888436007302L;

	private Buttons[] buttons;
	private String message;
	private String title;
	private ModalDialog parentDialog;

	public MessageDialogContent(String id, ModalDialog parentDialog,
			String title, String message, Buttons... buttons) {
		super(id);
		this.message = message;
		this.buttons = buttons;
		this.title = title;
		this.parentDialog = parentDialog;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		Form<Void> form = new Form<Void>("form");
		add(form);
		Label titleLabel = new Label("title", title);
		add(titleLabel);
		Label messageLabel = new Label("message", message);
		AjaxButton okButton = new AjaxButton("ok") {

			private static final long serialVersionUID = 8964562319862456903L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onConfirm(target);
				parentDialog.close(target);
			}
		};
		okButton.setVisible(Arrays.asList(buttons).contains(Buttons.OK));
		AjaxButton cancelButton = new AjaxButton("cancel") {

			private static final long serialVersionUID = 565534393678261505L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onCancel(target);
				parentDialog.close(target);
			}
		};
		cancelButton
				.setVisible(Arrays.asList(buttons).contains(Buttons.CANCEL));
		form.add(messageLabel, okButton, cancelButton);
	}

	protected abstract void onConfirm(AjaxRequestTarget target);

	protected abstract void onCancel(AjaxRequestTarget target);

	public enum Buttons {
		OK, CANCEL
	}
}

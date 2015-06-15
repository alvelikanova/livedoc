package com.livedoc.ui.common.components.dialogs;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public class ModalDialog extends ModalWindow {

	private static final long serialVersionUID = 835902908037077662L;

	public static final String CSS_CLASS = "modal-content";

	public ModalDialog(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		setCssClassName(CSS_CLASS);
		setResizable(false);
		setUseInitialHeight(false);
		showUnloadConfirmation(false);
	}

	@Override
	public void show(AjaxRequestTarget target) {
		super.show(target);
	}

	@Override
	protected ResourceReference newCssResource() {
		return new CssResourceReference(ModalDialog.class, "modal.css");
	}
}

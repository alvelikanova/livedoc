package com.livedoc.ui.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

public class StubAjaxUpdate extends AjaxFormComponentUpdatingBehavior {

	private static final long serialVersionUID = 3773432870005067826L;

	public StubAjaxUpdate() {
		super("change");
	}

	@Override
	protected void onUpdate(AjaxRequestTarget target) {
		// Stub
	}

}

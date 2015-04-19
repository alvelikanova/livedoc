package com.livedoc.ui.common.components.table;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;

public class ButtonToolbar<T, M> extends GenericPanel<T> {

	private static final long serialVersionUID = 2215355728220577050L;
	private CellFunctionsProvider<T, M> functionProvider;

	public ButtonToolbar(String id, IModel<T> model,
			CellFunctionsProvider<T, M> functionProvider) {
		super(id, model);
		Args.notNull(functionProvider, "CellFunctionsProvider");
		this.functionProvider = functionProvider;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		AjaxLink<Void> editButton = new AjaxLink<Void>("edit") {

			private static final long serialVersionUID = 1414426333356363698L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				Page page = functionProvider.edit(ButtonToolbar.this.getModel());
				setResponsePage(page);
			}
		};
		AjaxLink<Void> deleteButton = new AjaxLink<Void>("delete") {

			private static final long serialVersionUID = -7911855916551756240L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				//TODO confirmation dialog
				functionProvider.delete(ButtonToolbar.this.getModel());
			}
		};
		add(editButton, deleteButton);
	}

}

package com.livedoc.ui.common.components.table;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class TableNavigationToolbar extends AbstractToolbar {

	private static final long serialVersionUID = 3267204562304229513L;

	public TableNavigationToolbar(final DataTable<?, ?> table) {
		super(table);

		WebMarkupContainer span = new WebMarkupContainer("span");
		add(span);
		span.add(AttributeModifier.replace("colspan",
				new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return String.valueOf(table.getColumns().size())
								.intern();
					}
				}));

		span.add(new TablePagingNavigator("navigator", table));
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		setVisible(getTable().getPageCount() > 1);
	}
}

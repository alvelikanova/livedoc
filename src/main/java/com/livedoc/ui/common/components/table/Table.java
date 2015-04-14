package com.livedoc.ui.common.components.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class Table<T, M> extends Panel {

	private static final long serialVersionUID = -468694974028653538L;
	private Settings settings;
	private SortableDataProvider<T, M> dataProvider;

	public Table(String id, Settings settings,
			SortableDataProvider<T, M> dataProvider) {
		super(id);
		this.settings = settings;
		this.dataProvider = dataProvider;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		Map<String, String> properties = settings.getProperties();
		List<PropertyColumn<T, M>> columns = new ArrayList<PropertyColumn<T, M>>();
		for (String property : properties.keySet()) {
			columns.add(new PropertyColumn<T, M>(new Model<String>(properties
					.get(property)), property));
		}
		DataTable<T, M> table = new DataTable<T, M>("table", columns,
				dataProvider, settings.getRowCount());
		table.addTopToolbar(new HeadersToolbar<M>(table, dataProvider));
		table.addBottomToolbar(new NavigationToolbar(table));
		add(table);
	}
}

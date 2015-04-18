package com.livedoc.ui.common.components.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.livedoc.ui.common.components.table.Settings.SettingsItem;

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

		List<Settings.SettingsItem> properties = settings.getProperties();
		Collections.sort(properties, new Comparator<Settings.SettingsItem>() {

			public int compare(SettingsItem o1, SettingsItem o2) {
				return o1.getOrder().compareTo(o2.getOrder());
			}
		});
		List<PropertyColumn<T, M>> columns = new ArrayList<PropertyColumn<T, M>>();
		for (Settings.SettingsItem property : properties) {
			columns.add(new PropertyColumn<T, M>(new Model<String>(property
					.getColumnName()), property.getPropertyName()));
		}
		DataTable<T, M> table = new DataTable<T, M>("table", columns,
				dataProvider, settings.getRowCount());
		table.addTopToolbar(new HeadersToolbar<M>(table, dataProvider));
		table.addBottomToolbar(new TableNavigationToolbar(table));
		add(table);
	}
}

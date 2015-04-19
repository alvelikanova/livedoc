package com.livedoc.ui.common.components.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.livedoc.ui.common.components.table.Settings.SettingsItem;

public class Table<T, M> extends Panel {

	private static final long serialVersionUID = -468694974028653538L;
	private Settings settings;
	private SortableDataProvider<T, M> dataProvider;
	private CellFunctionsProvider<T, M> cellFunctionsProvider;

	public Table(String id, Settings settings,
			SortableDataProvider<T, M> dataProvider) {
		super(id);
		setOutputMarkupId(true);
		this.settings = settings;
		this.dataProvider = dataProvider;
	}

	public void setCellFunctionsProvider(
			CellFunctionsProvider<T, M> cellFunctionsProvider) {
		this.cellFunctionsProvider = cellFunctionsProvider;
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
		List<AbstractColumn<T, M>> columns = new ArrayList<AbstractColumn<T, M>>();
		for (Settings.SettingsItem property : properties) {
			columns.add(new PropertyColumn<T, M>(new Model<String>(property
					.getColumnName()), property.getPropertyName()));
		}
		if (settings.isIncludeButtons()) {
			AbstractColumn<T, M> buttonColumn = new AbstractColumn<T, M>(
					new Model<String>()) {
				private static final long serialVersionUID = 8220346662106986063L;

				public void populateItem(Item<ICellPopulator<T>> cellItem,
						String componentId, IModel<T> rowModel) {
					cellItem.add(new ButtonToolbar<T, M>(componentId, rowModel,
							cellFunctionsProvider));
				}
			};
			columns.add(buttonColumn);
		}
		DataTable<T, M> table = new DataTable<T, M>("table", columns,
				dataProvider, settings.getRowCount());
		table.addTopToolbar(new HeadersToolbar<M>(table, dataProvider));
		table.addBottomToolbar(new TableNavigationToolbar(table));
		add(table);
	}
}

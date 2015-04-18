package com.livedoc.ui.common.components.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Settings for Table
 * 
 * @see com.livedoc.ui.common.components.table.Table
 * 
 * @author velikanova.alena
 *
 */
public class Settings implements Serializable {

	private static final long serialVersionUID = -2656948132463806419L;

	private List<SettingsItem> properties = new ArrayList<SettingsItem>();
	private int rowCount;

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public List<SettingsItem> getProperties() {
		return properties;
	}

	public void addItem(String propertyName, String columnName, Integer order) {
		SettingsItem item = new SettingsItem(propertyName, columnName, order);
		properties.add(item);
	}

	class SettingsItem implements Serializable {
		private static final long serialVersionUID = -3732802084968459171L;

		private String propertyName;
		private String columnName;
		private Integer order;

		public SettingsItem(String propertyName, String columnName,
				Integer order) {
			super();
			this.propertyName = propertyName;
			this.columnName = columnName;
			this.order = order;
		}

		public String getPropertyName() {
			return propertyName;
		}

		public void setPropertyName(String propertyName) {
			this.propertyName = propertyName;
		}

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public Integer getOrder() {
			return order;
		}

		public void setOrder(Integer order) {
			this.order = order;
		}
	}
}

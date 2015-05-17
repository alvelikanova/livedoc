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
	private boolean includeButtons;

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public boolean isIncludeButtons() {
		return includeButtons;
	}

	public void setIncludeButtons(boolean includeButtons) {
		this.includeButtons = includeButtons;
	}

	public List<SettingsItem> getProperties() {
		return properties;
	}

	/**
	 * 
	 * @param propertyName
	 * @param columnName
	 * @param order
	 * @param width - may be from 1 to 11. Sum of element width must be 11. 
	 */
	public void addItem(String propertyName, String columnName, Integer order,
			Integer width) {
		SettingsItem item = new SettingsItem(propertyName, columnName, order,
				width);
		properties.add(item);
	}

	class SettingsItem implements Serializable {
		private static final long serialVersionUID = -3732802084968459171L;

		private String propertyName;
		private String columnName;
		private Integer order;
		private Integer width;

		public SettingsItem(String propertyName, String columnName,
				Integer order, Integer width) {
			super();
			this.propertyName = propertyName;
			this.columnName = columnName;
			this.order = order;
			this.width = width;
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

		public Integer getWidth() {
			return width;
		}

		public void setWidth(Integer width) {
			this.width = width;
		}
	}
}

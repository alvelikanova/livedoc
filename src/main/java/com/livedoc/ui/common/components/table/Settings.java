package com.livedoc.ui.common.components.table;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Settings for Table 
 * @see com.livedoc.ui.common.components.table.Table
 * 
 * @author velikanova.alena
 *
 */
public class Settings implements Serializable {

	private static final long serialVersionUID = -2656948132463806419L;
	
	/**
	 * Keys are domain object's properties, values are titles for columns 
	 */
	private Map<String, String> properties = new HashMap<String, String>();
	private int rowCount;

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}

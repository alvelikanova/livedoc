package com.livedoc.ui.common.components.table;

import java.io.Serializable;

import org.apache.wicket.Page;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;

public abstract class CellFunctionsProvider<T, M> implements Serializable {

	private static final long serialVersionUID = -2144246104317208151L;
	private Table<T, M> table;

	public CellFunctionsProvider(Table<T, M> table) {
		super();
		Injector.get().inject(this);
		this.table = table;
	}

	/**
	 * Constructs page with full information about object
	 * 
	 * @param model
	 * @return
	 */
	public abstract Page edit(IModel<T> model);

	/**
	 * Deletes object
	 * 
	 * @param model
	 */
	public abstract void delete(IModel<T> model);

	public Table<T, M> getTable() {
		return table;
	}
}

package com.livedoc.ui.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;

import com.livedoc.ui.common.components.SearchField;

public class HomePage extends MasterPage {

    private static final long serialVersionUID = -1682465878978520170L;

    @Override
    public void onInitialize() {
	super.onInitialize();

	// search field
	SearchField searchField = new SearchField("searchField") {

	    private static final long serialVersionUID = 8478544139621332281L;

	    @Override
	    protected void onSearch(AjaxRequestTarget target, String query) {
		HomePage.this.onSearch(target, query);
	    }
	};
	add(searchField);
    }

    protected void onSearch(AjaxRequestTarget target, String query) {

    }
}

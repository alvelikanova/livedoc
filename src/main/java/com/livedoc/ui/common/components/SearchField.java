package com.livedoc.ui.common.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public abstract class SearchField extends Panel {

    private static final long serialVersionUID = -5836896913510350601L;

    private String query;

    public SearchField(String id) {
	super(id);
    }

    @Override
    public void onInitialize() {
	super.onInitialize();

	Form<Void> form = new Form<Void>("form");
	add(form);
	TextField<String> searchField = new TextField<String>("search",
		new PropertyModel<String>(this, "query"));
	AjaxButton searchLink = new AjaxButton("searchLink") {
	    private static final long serialVersionUID = -4892751260754693457L;

	    @Override
	    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		onSearch(target, query);
	    }
	};
	form.add(searchField, searchLink);
	form.setDefaultButton(searchLink);
    }

    protected abstract void onSearch(AjaxRequestTarget target, String query);

    public String getQuery() {
	return query;
    }

    public void setQuery(String query) {
	this.query = query;
    }

}

package com.livedoc.ui.pages;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.springframework.security.core.context.SecurityContextHolder;

import com.livedoc.security.SecurityUserDetails;
import com.livedoc.ui.common.components.Header;

public class MasterPage extends WebPage {

    private static final long serialVersionUID = 8255562492976923335L;

    public MasterPage() {
	super();
	Injector.get().inject(this);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();
	add(new Header("header-panel", new Model<SecurityUserDetails>(
		getUserData())));
    }

    protected SecurityUserDetails getUserData() {
	return (SecurityUserDetails) SecurityContextHolder.getContext()
		.getAuthentication().getPrincipal();
    }
}

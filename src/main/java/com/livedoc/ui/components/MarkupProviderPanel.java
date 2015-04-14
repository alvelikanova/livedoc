package com.livedoc.ui.components;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

public class MarkupProviderPanel extends Panel implements IMarkupResourceStreamProvider, IMarkupCacheKeyProvider {

	private static final long serialVersionUID = 6002520159946390838L;

	private IModel<String> htmlResource;

	public MarkupProviderPanel(String id, IModel<String> model) {
		super(id);
		this.htmlResource = model;
	}

	public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
		StringBuilder sb = new StringBuilder("<wicket:panel>");
		sb.append(htmlResource.getObject());
		sb.append("</wicket:panel>");
		StringResourceStream resourceStream = new StringResourceStream(sb.toString());
		return resourceStream;
	}

	public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
		return null;
	}
}

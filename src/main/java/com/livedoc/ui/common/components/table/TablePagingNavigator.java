package com.livedoc.ui.common.components.table;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigationIncrementLink;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigationLink;
import org.apache.wicket.markup.html.panel.Panel;

public class TablePagingNavigator extends Panel {
	private static final long serialVersionUID = 4025317067762763328L;

	private PagingNavigation pagingNavigation;
	private final IPageable pageable;
	private final IPagingLabelProvider labelProvider;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param pageable
	 *            The pageable component the page links are referring to.
	 */
	public TablePagingNavigator(final String id, final IPageable pageable) {
		this(id, pageable, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param pageable
	 *            The pageable component the page links are referring to.
	 * @param labelProvider
	 *            The label provider for the link text.
	 */
	public TablePagingNavigator(final String id, final IPageable pageable,
			final IPagingLabelProvider labelProvider) {
		super(id);
		this.pageable = pageable;
		this.labelProvider = labelProvider;
	}

	/**
	 * {@link IPageable} this navigator is linked with
	 * 
	 * @return {@link IPageable} instance
	 */
	public final IPageable getPageable() {
		return pageable;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		pagingNavigation = new PagingNavigation("navigation", pageable,
				labelProvider);
		add(pagingNavigation);

		add(new PagingNavigationLink<Void>("first", pageable, 0));
		add(new PagingNavigationIncrementLink<Void>("prev", pageable, -1));
		add(new PagingNavigationIncrementLink<Void>("next", pageable, 1));
		add(new PagingNavigationLink<Void>("last", pageable, -1));
	}

	/**
	 * Gets the pageable navigation component for configuration purposes.
	 * 
	 * @return the associated pageable navigation.
	 */
	public final PagingNavigation getPagingNavigation() {
		return pagingNavigation;
	}
}

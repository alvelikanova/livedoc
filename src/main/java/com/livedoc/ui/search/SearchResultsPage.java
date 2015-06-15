package com.livedoc.ui.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.common.MessageException;
import com.livedoc.bl.common.SearchFieldEnum;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.domain.entities.DocumentPart;
import com.livedoc.bl.services.SearchService;
import com.livedoc.ui.pages.HomePage;

public class SearchResultsPage extends HomePage {

	private static final long serialVersionUID = 7748850467635903083L;

	// models
	private Map<DocumentPart, String> searchResults = new HashMap<DocumentPart, String>();

	// services
	@SpringBean
	private SearchService searchService;

	// components
	private RefreshingView<DocumentPart> searchResultList;
	private WebMarkupContainer resultsContainer;

	public SearchResultsPage(String query) {
		if (query != null) {
			onSearch(null, query);
		}
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		resultsContainer = new WebMarkupContainer("resultsContainer");
		add(resultsContainer);
		searchResultList = new RefreshingView<DocumentPart>("searchResultList") {

			private static final long serialVersionUID = -1091905108949031577L;

			@Override
			protected Iterator<IModel<DocumentPart>> getItemModels() {
				List<IModel<DocumentPart>> modelList = new ArrayList<IModel<DocumentPart>>();
				for (DocumentPart part : searchResults.keySet()) {
					IModel<DocumentPart> model = new Model<DocumentPart>(part);
					modelList.add(model);
				}
				Collections.sort(modelList,
						new Comparator<IModel<DocumentPart>>() {
							@Override
							public int compare(IModel<DocumentPart> o1,
									IModel<DocumentPart> o2) {
								DocumentData d1 = o1.getObject()
										.getDocumentData();
								DocumentData d2 = o2.getObject()
										.getDocumentData();
								if (d1.getId().equals(d2.getId())) {
									return o1.getObject().getOrder()
											- o2.getObject().getOrder();
								}
								return d1.getTitle().compareTo(d2.getTitle());
							}
						});
				return modelList.iterator();
			}

			@Override
			protected void populateItem(final Item<DocumentPart> item) {
				final String contentText = searchResults.get(item
						.getModelObject());
				Label title = new Label("title", new PropertyModel<String>(
						item.getModel(), "documentData.title"));
				Label details = new Label("details", new Model<String>() {
					private static final long serialVersionUID = 2546738890147216474L;

					@Override
					public String getObject() {
						DocumentPart part = item.getModelObject();
						StringBuilder sb = new StringBuilder("(");
						sb.append(getString("author"));
						sb.append(": ");
						sb.append(part.getDocumentData().getCreateUser()
								.getName());
						if (part.getOrder() != 0) {
							sb.append(", ");
							sb.append(getString("chapter"));
							sb.append(" ");
							sb.append(part.getOrder());
						}
						sb.append(")");
						return sb.toString();
					}
				});
				Label content = new Label("content", contentText);
				content.setEscapeModelStrings(false);
				AjaxLink<String> link = new AjaxLink<String>("linkToResult",
						new PropertyModel<String>(item.getModel(), "id")) {

					private static final long serialVersionUID = 907907203743687104L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub

					}
				};
				link.add(title, details);
				item.add(link, content);
			}
		};
		resultsContainer.setOutputMarkupId(true);
		resultsContainer.add(searchResultList);
	}

	protected void onSearch(AjaxRequestTarget target, String query) {
		try {
			Map<DocumentData, List<DocumentPart>> results = searchService
					.searchDocumentsByFieldAndQuery(query, getUserData()
							.getUser().getProjects(), SearchFieldEnum.AUTHOR,
							SearchFieldEnum.TITLE, SearchFieldEnum.CONTENT);
			searchResults = searchService.generateHighlightedText(results,
					query);
			if (target != null) {
				target.add(resultsContainer);
			}
		} catch (MessageException e) {
			error(getString(e.getMessageCode()));
		}
	}
}

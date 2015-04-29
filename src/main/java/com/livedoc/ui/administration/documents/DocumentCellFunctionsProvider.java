package com.livedoc.ui.administration.documents;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.services.DocumentService;
import com.livedoc.ui.common.components.table.CellFunctionsProvider;
import com.livedoc.ui.common.components.table.Table;

public class DocumentCellFunctionsProvider extends
		CellFunctionsProvider<DocumentData, String> {
	private static final long serialVersionUID = -6724701427801287995L;

	@SpringBean
	private DocumentService documentService;
	private WebPage pageToReturn;

	public DocumentCellFunctionsProvider(WebPage pageToReturn,
			Table<DocumentData, String> table) {
		super(table);
		this.pageToReturn = pageToReturn;
	}

	@Override
	public Page edit(IModel<DocumentData> model) {
		EditDocumentPage page = new EditDocumentPage(pageToReturn, model);
		return page;
	}

	@Override
	public void delete(IModel<DocumentData> model) {
		// TODO Auto-generated method stub

	}

}

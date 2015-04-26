package com.livedoc.ui.administration.documents;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.services.DocumentService;

public class DocumentProvider extends
		SortableDataProvider<DocumentData, String> {

	private static final long serialVersionUID = -8509098631265480066L;

	@SpringBean
	private DocumentService documentService;
	private Category category;

	public DocumentProvider(Category category) {
		super();
		Injector.get().inject(this);
		this.category = category;
	}

	protected List<DocumentData> getData() {
		return documentService.getDocumentsByCategory(category);
	}

	public Iterator<? extends DocumentData> iterator(long first, long count) {
		List<DocumentData> list = getData();
		long toIndex = first + count;
		if (toIndex > list.size()) {
			toIndex = list.size();
		}
		return list.subList((int) first, (int) toIndex).listIterator();
	}

	public long size() {
		return getData().size();
	}

	public IModel<DocumentData> model(DocumentData object) {
		return new Model<DocumentData>(object);
	}
}
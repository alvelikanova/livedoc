package com.livedoc.ui.documents;

import java.io.Serializable;
import java.util.List;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;

public class DocumentsCatalog implements Serializable {

	private static final long serialVersionUID = 4944967716982934362L;

	private List<Category> categories;
	private DocumentData selectedDocument;

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public DocumentData getSelectedDocument() {
		return selectedDocument;
	}

	public void setSelectedDocument(DocumentData selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

}

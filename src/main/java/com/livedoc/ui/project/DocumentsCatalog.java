package com.livedoc.ui.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.DocumentData;

public class DocumentsCatalog implements Serializable {

	private static final long serialVersionUID = 4944967716982934362L;

	private List<Category> categories = new ArrayList<Category>();
	private DocumentData selectedDocument;
	private List<Document> chapters = new ArrayList<Document>();

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

	public List<Document> getChapters() {
		return chapters;
	}

	public void setChapters(List<Document> chapters) {
		this.chapters = chapters;
	}

}

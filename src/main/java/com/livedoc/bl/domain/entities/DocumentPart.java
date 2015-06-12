package com.livedoc.bl.domain.entities;

import java.util.ArrayList;
import java.util.List;

public class DocumentPart extends BaseDomainEntity {

	private static final long serialVersionUID = -4095836844694071147L;
	
	private DocumentData documentData;
	private int order;
	private String content;
	private String rootElementType;
	private List<Category> comments = new ArrayList<Category>();

	public DocumentPart() {
		super();
	}

	public DocumentPart(String id, DocumentData documentData, int order,
			String content, String rootElementType, List<Category> comments) {
		super(id);
		this.documentData = documentData;
		this.order = order;
		this.content = content;
		this.rootElementType = rootElementType;
		this.comments = comments;
	}

	public DocumentData getDocumentData() {
		return documentData;
	}

	public void setDocumentData(DocumentData documentData) {
		this.documentData = documentData;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRootElementType() {
		return rootElementType;
	}

	public void setRootElementType(String rootElementType) {
		this.rootElementType = rootElementType;
	}

	public List<Category> getComments() {
		return comments;
	}

	public void setComments(List<Category> comments) {
		this.comments = comments;
	}
}

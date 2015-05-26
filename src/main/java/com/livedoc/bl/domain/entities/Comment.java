package com.livedoc.bl.domain.entities;

import java.util.Date;

public class Comment extends BaseDomainEntity {

	private static final long serialVersionUID = -4404465684129949193L;
	
	private String documentPartId;
	private User author;
	private Date submitDate;
	private String comment;

	public Comment() {
		super();
	}

	public Comment(String id, String documentPartId, User author,
			Date submitDate, String comment) {
		super(id);
		this.documentPartId = documentPartId;
		this.author = author;
		this.submitDate = submitDate;
		this.comment = comment;
	}

	public String getDocumentPartId() {
		return documentPartId;
	}

	public void setDocumentPartId(String documentPartId) {
		this.documentPartId = documentPartId;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}

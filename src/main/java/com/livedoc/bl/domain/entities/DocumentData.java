package com.livedoc.bl.domain.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentData extends BaseDomainEntity {

	private static final long serialVersionUID = 658742102667348646L;

	private String title;
	private Date createDate;
	private User createUser;
	private Date lastModDate;
	private User lastModUser;
	private String description;
	private Category category;
	private String rootElement;
	private List<DocumentPart> parts = new ArrayList<DocumentPart>();

	public DocumentData() {
		super();
	}

	public DocumentData(String id, String title, Date createDate,
			User createUser) {
		super(id);
		this.title = title;
		this.createDate = createDate;
		this.createUser = createUser;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getLastModDate() {
		return lastModDate;
	}

	public void setLastModDate(Date lastModDate) {
		this.lastModDate = lastModDate;
	}

	public User getLastModUser() {
		return lastModUser;
	}

	public void setLastModUser(User lastModUser) {
		this.lastModUser = lastModUser;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getRootElement() {
		return rootElement;
	}

	public void setRootElement(String rootElement) {
		this.rootElement = rootElement;
	}

	public List<DocumentPart> getParts() {
		return parts;
	}

	public void setParts(List<DocumentPart> parts) {
		this.parts = parts;
	}

}

package com.livedoc.bl.domain.entities;

import java.util.Date;

public class DocumentData extends BaseDomainEntity {

	private static final long serialVersionUID = 658742102667348646L;

	private String title;
	private Date createDate;
	private User createUser;
	private Project project;

	public DocumentData() {
		super();
	}

	public DocumentData(String id, String title, Date createDate, User createUser, Project project) {
		super(id);
		this.title = title;
		this.createDate = createDate;
		this.createUser = createUser;
		this.project = project;
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}

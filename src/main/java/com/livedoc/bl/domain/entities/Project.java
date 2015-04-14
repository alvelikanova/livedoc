package com.livedoc.bl.domain.entities;

public class Project extends BaseDomainEntity {

	private static final long serialVersionUID = 8937120059057256554L;

	private String name;
	private String description;

	public Project() {
		super();
	}

	public Project(String id, String name) {
		super(id);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

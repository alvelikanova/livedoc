package com.livedoc.bl.domain.entities;

public class Category extends BaseDomainEntity {
	private static final long serialVersionUID = -7570534114497194327L;
	private String name;
	private String description;
	private Project project;

	public Category() {
		super();
	}

	public Category(String id, String name, String description, Project project) {
		super(id);
		this.name = name;
		this.description = description;
		this.project = project;
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}

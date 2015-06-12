package com.livedoc.bl.domain.entities;

import java.util.ArrayList;
import java.util.List;

public class User extends BaseDomainEntity {

	private static final long serialVersionUID = 1847448007783747496L;

	private String name;
	private String password;
	private Role role;
	private List<Project> projects = new ArrayList<Project>();

	public User() {
		super();
	}

	public User(String id, String name, String password) {
		super(id);
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
}

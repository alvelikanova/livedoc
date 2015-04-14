package com.livedoc.dal.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "project", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = "project_name"))
public class ProjectEntity extends BaseDalEntity {

	private static final long serialVersionUID = 7466560433927242351L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "project_id", unique = true, nullable = false, length = 32)
	private String projectId;

	@Column(name = "project_name", unique = true, nullable = false, length = 32)
	private String projectName;

	@Column(name = "project_description", length = 256)
	private String projectDescription;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
	private Set<DocumentDataEntity> documentDataEntities = new HashSet<DocumentDataEntity>(0);

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "projects")
	private Set<UserEntity> users = new HashSet<UserEntity>(0);

	public ProjectEntity() {
	}

	public ProjectEntity(String projectId, String projectName) {
		this.projectId = projectId;
		this.projectName = projectName;
	}

	public ProjectEntity(String projectId, String projectName, Set<DocumentDataEntity> documentDataEntities,
			Set<UserEntity> users) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.documentDataEntities = documentDataEntities;
		this.users = users;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Set<DocumentDataEntity> getDocumentDataEntities() {
		return documentDataEntities;
	}

	public void setDocumentDataEntities(Set<DocumentDataEntity> documentDataEntities) {
		this.documentDataEntities = documentDataEntities;
	}

	public Set<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

}

package com.livedoc.dal.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "users", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = "users_name"))
public class UserEntity extends BaseDalEntity {

	private static final long serialVersionUID = 3552434741923308873L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "users_id", unique = true, nullable = false, length = 32)
	private String userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_role_id", nullable = false)
	private RoleEntity role;

	@Column(name = "users_name", unique = true, nullable = false, length = 32)
	private String username;

	@Column(name = "users_password", nullable = false, length = 64)
	private String password;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "users_to_project", joinColumns = { @JoinColumn(name = "USERS_ID", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "PROJECT_ID", nullable = false) })
	private Set<ProjectEntity> projects = new HashSet<ProjectEntity>();

	public UserEntity() {
	}

	public UserEntity(String userId, RoleEntity role, String username,
			String password) {
		this.userId = userId;
		this.role = role;
		this.username = username;
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<ProjectEntity> getProjects() {
		return projects;
	}

	public void setProjects(Set<ProjectEntity> projects) {
		this.projects = projects;
	}
}

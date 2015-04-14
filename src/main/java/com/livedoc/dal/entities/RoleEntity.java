package com.livedoc.dal.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "user_role", schema = "public", uniqueConstraints = { @UniqueConstraint(columnNames = "user_role_code"),
		@UniqueConstraint(columnNames = "user_role_name") })
public class RoleEntity extends BaseDalEntity {

	private static final long serialVersionUID = 879282587706420278L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "user_role_id", unique = true, nullable = false, length = 32)
	private String roleId;

	@Column(name = "user_role_code", unique = true, nullable = false, length = 16)
	private String roleCode;

	@Column(name = "user_role_name", unique = true, nullable = false, length = 32)
	private String roleName;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
	private Set<UserEntity> users = new HashSet<UserEntity>(0);

	public RoleEntity() {
	}

	public RoleEntity(String roleId, String roleCode, String roleName) {
		this.roleId = roleId;
		this.roleCode = roleCode;
		this.roleName = roleName;
	}

	public RoleEntity(String roleId, String roleCode, String roleName, Set<UserEntity> users) {
		this.roleId = roleId;
		this.roleCode = roleCode;
		this.roleName = roleName;
		this.users = users;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}

}

package com.livedoc.bl.domain.entities;

public class Role extends BaseDomainEntity {

	private static final long serialVersionUID = 2216006487701367275L;

	private String name;
	private String code;

	public Role() {
		super();
	}

	public Role(String id, String name, String code) {
		super(id);
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}

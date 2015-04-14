package com.livedoc.bl.domain.entities;

import java.io.Serializable;

public class BaseDomainEntity implements Serializable {

	private static final long serialVersionUID = -7653543429131676811L;
	private String id;

	public BaseDomainEntity() {
		super();
	}
	
	public BaseDomainEntity(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

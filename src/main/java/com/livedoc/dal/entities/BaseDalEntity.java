package com.livedoc.dal.entities;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

/**
 * Base class for entities.
 * 
 * @author velikanova.alena
 *
 */
@MappedSuperclass
public class BaseDalEntity implements Serializable {
	private static final long serialVersionUID = -3062585628919434690L;

}

package com.livedoc.dal.providers;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;

import com.livedoc.dal.entities.BaseDalEntity;

public interface GenericDataProvider<T extends BaseDalEntity, ID extends Serializable> {

	T findById(ID id);
	
	List<T> findAll();

	List<T> findByCriterion(Criterion criterion);

	Session getSession();

	T saveOrUpdate(T entity);

	void delete(T entity);

}

package com.livedoc.dal.providers.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.dal.entities.BaseDalEntity;
import com.livedoc.dal.providers.GenericDataProvider;

@Transactional
public class BaseDataProvider<T extends BaseDalEntity, ID extends Serializable>
		implements GenericDataProvider<T, ID> {

	@Autowired
	protected SessionFactory sessionFactory;

	private final Class<T> entityClass;

	public BaseDataProvider(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public T findById(ID id) {
		Session session = sessionFactory.getCurrentSession();
		return (T) session.load(entityClass, id);
	}
	
	public List<T> findAll() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		return criteria.list();
	}

	public List<T> findByCriterion(Criterion criterion) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass).add(criterion);
		return criteria.list();
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public T saveOrUpdate(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(entity);
		return entity;
	}

	public void delete(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(entity);
	}

}

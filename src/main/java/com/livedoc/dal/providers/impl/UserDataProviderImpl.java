package com.livedoc.dal.providers.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.dal.entities.UserEntity;
import com.livedoc.dal.providers.UserDataProvider;

@Repository
@Transactional
public class UserDataProviderImpl extends BaseDataProvider<UserEntity, String>
		implements UserDataProvider {

	public UserDataProviderImpl() {
		super(UserEntity.class);
	}

	@Transactional
	public UserEntity findByLoginName(String loginName) {
		Session session = sessionFactory.getCurrentSession();
		Criterion criterion = Restrictions.eq("username", loginName);
		return (UserEntity) session.createCriteria(UserEntity.class)
				.add(criterion).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findUsers(List<String> excludeUsersIds) {
		Session session = sessionFactory.getCurrentSession();
		Criterion criterion = Restrictions.not(Restrictions.in("userId", excludeUsersIds));
		Criteria cr = session.createCriteria(UserEntity.class).add(criterion);
		return cr.list();
	}
}

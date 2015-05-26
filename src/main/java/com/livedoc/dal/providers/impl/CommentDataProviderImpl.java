package com.livedoc.dal.providers.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.dal.entities.CommentEntity;
import com.livedoc.dal.providers.CommentDataProvider;

@Repository
@Transactional
public class CommentDataProviderImpl extends
		BaseDataProvider<CommentEntity, String> implements CommentDataProvider {

	public CommentDataProviderImpl() {
		super(CommentEntity.class);
	}

}

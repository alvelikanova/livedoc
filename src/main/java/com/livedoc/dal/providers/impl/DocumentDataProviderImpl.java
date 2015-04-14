package com.livedoc.dal.providers.impl;

import org.springframework.stereotype.Repository;

import com.livedoc.dal.entities.DocumentDataEntity;
import com.livedoc.dal.providers.DocumentDataProvider;

@Repository
public class DocumentDataProviderImpl extends BaseDataProvider<DocumentDataEntity, String> implements
		DocumentDataProvider {

	public DocumentDataProviderImpl() {
		super(DocumentDataEntity.class);
	}

}

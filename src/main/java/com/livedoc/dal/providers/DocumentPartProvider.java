package com.livedoc.dal.providers;

import java.util.List;

import com.livedoc.dal.entities.DocumentPartEntity;

public interface DocumentPartProvider extends
		GenericDataProvider<DocumentPartEntity, String> {

	List<DocumentPartEntity> getPartsOfDocument(String documentId);

}

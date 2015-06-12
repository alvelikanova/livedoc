package com.livedoc.dal.providers;

import com.livedoc.dal.entities.DocumentPartEntity;

public interface DocumentPartProvider extends
		GenericDataProvider<DocumentPartEntity, String> {

	DocumentPartEntity getChapter(String documentId, int order);

}

package com.livedoc.dal.providers;

import java.util.List;

import com.livedoc.dal.entities.DocumentDataEntity;

public interface DocumentDataProvider extends
		GenericDataProvider<DocumentDataEntity, String> {

	List<DocumentDataEntity> getDocumentsByCategoryId(String categoryId);

	int countDocumentsOfCategory(String categoryId);

}

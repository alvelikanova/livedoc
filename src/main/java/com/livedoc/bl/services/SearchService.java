package com.livedoc.bl.services;

import java.util.List;
import java.util.Map;

import com.livedoc.bl.common.MessageException;
import com.livedoc.bl.common.SearchFieldEnum;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.domain.entities.DocumentPart;
import com.livedoc.bl.domain.entities.Project;

public interface SearchService {

	void addDocumentToIndex(DocumentData documentData) throws MessageException;

	void removeDocumentFromIndex(DocumentData documentData)
			throws MessageException;

	void updateIndexedDocument(DocumentData documentData)
			throws MessageException;

	void deleteAllIndicesOfProject(Project project) throws MessageException;

	/**
	 * TODO add javadoc
	 * 
	 * @param query
	 *            - a search string entered by user
	 * @param projects
	 *            - projects which user is subscribed to
	 * @param fields
	 *            - where to search (content, author, title)
	 * @return search result map
	 * @throws MessageException
	 */
	Map<DocumentData, List<DocumentPart>> searchDocumentsByFieldAndQuery(
			String query, List<Project> projects, SearchFieldEnum... fields)
			throws MessageException;

	/**
	 * TODO add javadoc
	 * 
	 * @param searchResultMap
	 *            - result derived from
	 *            {@link #searchDocumentsByFieldAndQuery(String, List, SearchFieldEnum...)}
	 * @param query
	 *            - a search string entered by user
	 * @return map: keys - document part, values - highlighted content
	 */
	Map<DocumentPart, String> generateHighlightedText(
			Map<DocumentData, List<DocumentPart>> searchResultMap,
			String queryString);
}

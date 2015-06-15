package com.livedoc.bl.services.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.livedoc.bl.common.MessageException;
import com.livedoc.bl.common.SearchFieldEnum;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.domain.entities.DocumentPart;
import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.services.DocumentService;
import com.livedoc.bl.services.SearchService;

@Service
public class SearchServiceImpl implements SearchService {
	private static final Logger logger = Logger
			.getLogger(SearchServiceImpl.class);

	// constants
	private static final String INDICES_PATH = "/lucene/indices/";
	private static final String HIGHLIGHT_CSS = "bg-primary";
	private static final String SEARCH_RESULTS_SEPARATOR = "...";
	private static final int MAX_LENGTH = 50;
	private static final int MAX_RESULTS = 10;

	// error codes
	private static final String IND_UPDATE = "IND_UPD_ERROR";
	private static final String IND_DELETE = "IND_DEL_ERROR";
	private static final String IND_READ = "IND_READ_ERROR";
	private static final String MALFORM_QUERY = "MALFORM_QUERY";

	private Directory directory;
	private Map<String, DocumentData> docDataCache = new HashMap<String, DocumentData>();
	private Map<String, DocumentPart> docPartCache = new HashMap<String, DocumentPart>();

	// services
	@Autowired
	private DocumentService documentService;

	public void addDocumentToIndex(DocumentData document)
			throws MessageException {
		if (document == null || document.getId() == null) {
			logger.warn("Null document or document with null id has been retreived");
			return;
		}
		try {
			IndexWriter writer = getIndexWriter(document);
			for (DocumentPart part : document.getParts()) {
				Document indexedDocument = new Document();
				indexedDocument.add(new StringField(SearchFieldEnum.ID
						.getName(), part.getId(), Field.Store.YES));
				indexedDocument.add(new StringField(SearchFieldEnum.PARENT_ID
						.getName(), part.getDocumentData().getId(),
						Field.Store.YES));
				indexedDocument.add(new TextField(SearchFieldEnum.CONTENT
						.getName(), part.getContent(), Field.Store.NO));
				indexedDocument.add(new StringField(SearchFieldEnum.AUTHOR
						.getName(), document.getCreateUser().getName(),
						Field.Store.YES));
				indexedDocument.add(new TextField(SearchFieldEnum.TITLE
						.getName(), document.getTitle(), Field.Store.YES));
				writer.addDocument(indexedDocument);
			}
			writer.close();
		} catch (IOException e) {
			logger.error("I\\O Error when trying to open indices directory", e);
			MessageException ex = new MessageException();
			ex.setMessageCode(IND_UPDATE);
			throw ex;
		}
	}

	private IndexWriter getIndexWriter(DocumentData document)
			throws MessageException {
		String projectId = document.getCategory().getProject().getId();
		try {
			Path path = preparePathForProject(projectId);
			if (!Files.exists(path,
					new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
				Files.createDirectory(path);
			}
			directory = new NIOFSDirectory(path);
			StandardAnalyzer analyzer = new StandardAnalyzer();
			return new IndexWriter(directory, new IndexWriterConfig(analyzer));
		} catch (IOException e) {
			logger.error(
					"I\\O Error when trying to create indexwriter in specified directory, projectId: "
							+ projectId, e);
			MessageException ex = new MessageException();
			ex.setMessageCode(IND_UPDATE);
			throw ex;
		}
	}

	public Map<DocumentData, List<DocumentPart>> searchDocumentsByFieldAndQuery(
			String query, List<Project> projects, SearchFieldEnum... fields)
			throws MessageException {
		try {
			Map<DocumentData, List<DocumentPart>> searchMap = new HashMap<DocumentData, List<DocumentPart>>() {
				private static final long serialVersionUID = 6785021204759176145L;

				@Override
				public List<DocumentPart> get(Object key) {
					if (!this.containsKey(key)) {
						this.put((DocumentData) key,
								new ArrayList<DocumentPart>());
					}
					return super.get(key);
				}
			};
			for (Project project : projects) {
				Path path = preparePathForProject(project.getId());
				if (!Files.exists(path,
						new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
					logger.warn("No directory found for project with id "
							+ project.getId());
					continue;
				}
				directory = new NIOFSDirectory(path);
				IndexReader reader = DirectoryReader.open(directory);
				for (SearchFieldEnum field : fields) {
					IndexSearcher searcher = new IndexSearcher(reader);
					QueryParser parser = new QueryParser(field.getName(),
							new SimpleAnalyzer());
					Query luceneQuery = parser.parse(query);
					TopDocs docs = searcher.search(luceneQuery, 10);
					for (ScoreDoc doc : docs.scoreDocs) {
						// something is found
						Document d = searcher.doc(doc.doc);
						switch (field) {
						case AUTHOR:
						case TITLE: {
							String documentId = d.get(SearchFieldEnum.PARENT_ID
									.getName());
							searchMap.put(getDocumentDataFromCache(documentId),
									new ArrayList<DocumentPart>());
						}
							break;
						case CONTENT: {
							String documentId = d.get(SearchFieldEnum.PARENT_ID
									.getName());
							String documentPartId = d.get(SearchFieldEnum.ID
									.getName());
							searchMap
									.get(getDocumentDataFromCache(documentId))
									.add(getDocumentPartFromCache(documentPartId));
						}
							break;
						default:
							logger.warn("Unsupported search field has been received: "
									+ field.getName());
						}
					}
				}
				reader.close();
			}
			return searchMap;
		} catch (IOException e) {
			logger.error("I\\O Error when trying to open indices directory", e);
			MessageException ex = new MessageException();
			ex.setMessageCode(IND_READ);
			throw ex;
		} catch (ParseException e) {
			logger.error("Error when trying to parse query", e);
			MessageException ex = new MessageException();
			ex.setMessageCode(MALFORM_QUERY);
			throw ex;
		}
	}

	private Path preparePathForProject(String projectId)
			throws MessageException {
		System.out.println(new File(".").getAbsolutePath());
		URL generalPath = SearchServiceImpl.class.getClassLoader().getResource(
				INDICES_PATH);
		if (generalPath == null) {
			logger.error("Could not find root indices directory");
			MessageException ex = new MessageException();
			ex.setMessageCode(IND_READ);
			throw ex;
		}
		String filePath = generalPath.getFile();
		logger.info("Generated path is: " + filePath);
		Path path = Paths.get(filePath.substring(1, filePath.length())
				+ projectId + "/");
		return path;
	}

	private DocumentData getDocumentDataFromCache(String documentId) {
		if (!docDataCache.containsKey(documentId)) {
			DocumentData docData = documentService.getDocumentById(documentId);
			docDataCache.put(documentId, docData);
		}
		return docDataCache.get(documentId);
	}

	private DocumentPart getDocumentPartFromCache(String documentPartId) {
		if (!docPartCache.containsKey(documentPartId)) {
			DocumentPart docPart = documentService
					.getDocumentPartById(documentPartId);
			docPartCache.put(documentPartId, docPart);
		}
		return docPartCache.get(documentPartId);
	}

	public Map<DocumentPart, String> generateHighlightedText(
			Map<DocumentData, List<DocumentPart>> searchResultMap,
			String queryString) {
		Map<DocumentPart, String> resultMap = new HashMap<DocumentPart, String>();
		for (Entry<DocumentData, List<DocumentPart>> entry : searchResultMap
				.entrySet()) {
			List<DocumentPart> foundParts = entry.getValue();
			if (CollectionUtils.isEmpty(foundParts)) {
				// if the value is empty then the search string was found in
				// title or author
				// put the first document part id in the map and leave non
				// highlighted
				DocumentData docData = entry.getKey();
				List<DocumentPart> parts = docData.getParts();
				Collections.sort(parts, new Comparator<DocumentPart>() {

					public int compare(DocumentPart o1, DocumentPart o2) {
						return o1.getOrder() - o2.getOrder();
					}
				});
				DocumentPart firstPart = parts.get(0);
				resultMap.put(firstPart,
						getPlainTextFromDocumentPart(firstPart));
			} else {
				for (DocumentPart part : foundParts) {
					String plainText = getPlainTextFromDocumentPart(part);
					SimpleHTMLFormatter formatter = new SimpleHTMLFormatter(
							"<span class=\"" + HIGHLIGHT_CSS + "\">", "</span>");
					try (StringReader stringReader = new StringReader(plainText);
							TokenStream tokenStream = new SimpleAnalyzer()
									.tokenStream(
											SearchFieldEnum.CONTENT.getName(),
											stringReader)) {
						QueryParser parser = new QueryParser(
								SearchFieldEnum.CONTENT.getName(),
								new StandardAnalyzer());
						Query query = parser.parse(queryString);
						QueryScorer queryScorer = new QueryScorer(query,
								SearchFieldEnum.CONTENT.getName());
						Fragmenter fragmenter = new SimpleSpanFragmenter(
								queryScorer, MAX_LENGTH);
						Highlighter highlighter = new Highlighter(formatter,
								queryScorer);
						highlighter.setTextFragmenter(fragmenter);
						String result = highlighter.getBestFragments(
								tokenStream, plainText, MAX_RESULTS,
								SEARCH_RESULTS_SEPARATOR);
						resultMap.put(part, result);

					} catch (IOException e) {
						logger.warn(
								"Error occured while trying to read plain text",
								e);
					} catch (ParseException e) {
						logger.warn(
								"Error occured while trying to parse query entered by user",
								e);
					} catch (InvalidTokenOffsetsException e) {
						logger.warn(
								"Error occured while trying to retreive search results",
								e);
					}
				}
			}

		}
		return resultMap;
	}

	private String getPlainTextFromDocumentPart(DocumentPart part) {
		String plainText = "";
		String content;
		if (part == null || StringUtils.isBlank(content = part.getContent())) {
			return "";
		}
		plainText = Jsoup.parse(content, "", Parser.xmlParser()).text();
		return plainText;
	}

	@Override
	public void removeDocumentFromIndex(DocumentData document)
			throws MessageException {
		try {
			IndexWriter writer = getIndexWriter(document);
			List<DocumentPart> parts = document.getParts();
			for (DocumentPart part : parts) {
				writer.deleteDocuments(new Term(SearchFieldEnum.ID.getName(),
						part.getId()));
			}
			writer.close();
		} catch (IOException e) {
			logger.warn("Failed to delete indices of document with id: "
					+ document.getId());
		}
	}

	@Override
	public void updateIndexedDocument(DocumentData document)
			throws MessageException {
		if (document == null || document.getId() == null) {
			logger.warn("Null document or document with null id has been retreived");
			return;
		}
		try {
			IndexWriter writer = getIndexWriter(document);
			for (DocumentPart part : document.getParts()) {
				Document indexedDocument = new Document();
				indexedDocument.add(new StringField(SearchFieldEnum.ID
						.getName(), part.getId(), Field.Store.YES));
				indexedDocument.add(new StringField(SearchFieldEnum.PARENT_ID
						.getName(), part.getDocumentData().getId(),
						Field.Store.YES));
				indexedDocument.add(new TextField(SearchFieldEnum.CONTENT
						.getName(), part.getContent(), Field.Store.NO));
				indexedDocument.add(new StringField(SearchFieldEnum.AUTHOR
						.getName(), document.getCreateUser().getName(),
						Field.Store.YES));
				indexedDocument.add(new TextField(SearchFieldEnum.TITLE
						.getName(), document.getTitle(), Field.Store.YES));
				writer.updateDocument(new Term(SearchFieldEnum.ID.getName(),
						part.getId()), indexedDocument);
			}
			writer.close();
		} catch (IOException e) {
			logger.error("I\\O Error when trying to open indices directory", e);
			MessageException ex = new MessageException();
			ex.setMessageCode(IND_DELETE);
			throw ex;
		}
	}

	@Override
	public void deleteAllIndicesOfProject(Project project)
			throws MessageException {
		Path path = preparePathForProject(project.getId());
		if (Files.exists(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
			try {
				Files.walkFileTree(path, new FileVisitor<Path>() {

					@Override
					public FileVisitResult preVisitDirectory(Path dir,
							BasicFileAttributes attrs) throws IOException {
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFile(Path file,
							BasicFileAttributes attrs) throws IOException {
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFileFailed(Path file,
							IOException exc) throws IOException {
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(Path dir,
							IOException exc) throws IOException {
						Files.delete(dir);
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (IOException e) {
				logger.error("Error ocured while deleting directory");
			}
		}
	}
}

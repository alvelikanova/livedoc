package com.livedoc.bl.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.bl.domain.entities.Comment;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.services.CommentService;
import com.livedoc.dal.entities.CommentEntity;
import com.livedoc.dal.entities.DocumentPartEntity;
import com.livedoc.dal.providers.CommentDataProvider;
import com.livedoc.dal.providers.DocumentPartProvider;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

	@Autowired
	private DocumentPartProvider documentPartProvider;

	@Autowired
	private CommentDataProvider commentDataProvider;

	@Autowired
	private DozerBeanMapper mapper;

	public List<Comment> getCommentsByDocumentAndChapterOrder(
			DocumentData docData, int order) {
		DocumentPartEntity chapters = documentPartProvider.getChapter(
				docData.getId(), order);
		Set<CommentEntity> commentEntities = chapters.getComments();
		List<Comment> comments = new ArrayList<Comment>();
		for (CommentEntity commentEntity : commentEntities) {
			Comment comment = mapper.map(commentEntity, Comment.class);
			comments.add(comment);
		}
		return comments;
	}

	public Comment saveComment(Comment comment, String chapterId) {
		if (chapterId == null) {
			return null;
		}
		CommentEntity entity = mapper.map(comment, CommentEntity.class);
		DocumentPartEntity part = documentPartProvider.findById(chapterId);
		entity.setDocPart(part);
		CommentEntity persistedEntity = commentDataProvider
				.saveOrUpdate(entity);
		Comment persistedComment = mapper.map(persistedEntity, Comment.class);
		return persistedComment;
	}

}

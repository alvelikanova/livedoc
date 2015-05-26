package com.livedoc.bl.services;

import java.util.List;

import com.livedoc.bl.domain.entities.Comment;
import com.livedoc.bl.domain.entities.DocumentData;

public interface CommentService {

	List<Comment> getCommentsByDocumentAndChapterOrder(DocumentData docData,
			int order);

	Comment saveComment(Comment comment, String chapterId);

}

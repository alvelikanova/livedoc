package com.livedoc.dal.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "comment", schema = "public")
public class CommentEntity extends BaseDalEntity {

	private static final long serialVersionUID = 3869724205127979559L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "comment_id", unique = true, nullable = false, length = 32)
	private String commentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_doc_part_id", nullable = false)
	private DocumentPartEntity docPart;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_author_id")
	private UserEntity author;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "comment_submit_ts", nullable = false, length = 35)
	private Date commentSubmitTs;

	@Column(name = "comment_content", nullable = false, length = 256)
	private String commentContent;

	public CommentEntity() {
		super();
	}

	public CommentEntity(String commentId, DocumentPartEntity docPart,
			UserEntity author, Date commentSubmitTs, String commentContent) {
		super();
		this.commentId = commentId;
		this.docPart = docPart;
		this.author = author;
		this.commentSubmitTs = commentSubmitTs;
		this.commentContent = commentContent;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public DocumentPartEntity getDocPart() {
		return docPart;
	}

	public void setDocPart(DocumentPartEntity docPart) {
		this.docPart = docPart;
	}

	public UserEntity getAuthor() {
		return author;
	}

	public void setAuthor(UserEntity author) {
		this.author = author;
	}

	public Date getCommentSubmitTs() {
		return commentSubmitTs;
	}

	public void setCommentSubmitTs(Date commentSubmitTs) {
		this.commentSubmitTs = commentSubmitTs;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

}

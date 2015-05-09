package com.livedoc.dal.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "doc_part", schema = "public")
public class DocumentPartEntity extends BaseDalEntity {

	private static final long serialVersionUID = -5420073724328175756L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "doc_part_id", unique = true, nullable = false, length = 32)
	private String documentPartId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_part_doc_data_id", nullable = false)
	private DocumentDataEntity documentData;

	@Column(name = "doc_part_order", nullable = false)
	private int documentPartOrder;

	@Column(name = "doc_part_content")
	private String documentPartContent;

	public DocumentPartEntity() {
	}

	public DocumentPartEntity(String documentPartId,
			DocumentDataEntity documentData, int documentPartOrder,
			String documentPartContent) {
		super();
		this.documentPartId = documentPartId;
		this.documentData = documentData;
		this.documentPartOrder = documentPartOrder;
		this.documentPartContent = documentPartContent;
	}

	public String getDocumentPartId() {
		return documentPartId;
	}

	public void setDocumentPartId(String documentPartId) {
		this.documentPartId = documentPartId;
	}

	public DocumentDataEntity getDocumentData() {
		return documentData;
	}

	public void setDocumentData(DocumentDataEntity documentData) {
		this.documentData = documentData;
	}

	public int getDocumentPartOrder() {
		return documentPartOrder;
	}

	public void setDocumentPartOrder(int documentPartOrder) {
		this.documentPartOrder = documentPartOrder;
	}

	public String getDocumentPartContent() {
		return documentPartContent;
	}

	public void setDocumentPartContent(String documentPartContent) {
		this.documentPartContent = documentPartContent;
	}

}

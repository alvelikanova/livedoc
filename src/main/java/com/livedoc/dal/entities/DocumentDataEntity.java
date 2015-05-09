package com.livedoc.dal.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "doc_data", schema = "public")
public class DocumentDataEntity extends BaseDalEntity {

	private static final long serialVersionUID = 6172583611533555194L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "doc_data_id", unique = true, nullable = false, length = 32)
	private String documentDataId;

	@Column(name = "doc_data_title", nullable = false, length = 64)
	private String documentTitle;

	@Column(name = "doc_data_description", length = 256)
	private String docDataDescription;

	@Column(name = "doc_data_root_elem_type", length = 16)
	private String rootElementType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_data_create_user_id", nullable = false)
	private UserEntity createUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "doc_data_create_ts", nullable = false, length = 35)
	private Date documentCreationTs;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_data_last_mod_user_id", nullable = false)
	private UserEntity lastModUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "doc_data_last_mod_ts", nullable = false, length = 35)
	private Date documentModTs;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_data_category_id", nullable = false)
	private CategoryEntity category;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "documentData", cascade = {CascadeType.ALL})
	private Set<DocumentPartEntity> parts = new HashSet<DocumentPartEntity>(0);

	public DocumentDataEntity() {
		super();
	}

	public DocumentDataEntity(String documentDataId, String documentTitle,
			String docDataDescription, UserEntity createUser,
			Date documentCreationTs, UserEntity lastModUser,
			Date documentModTs, CategoryEntity category) {
		super();
		this.documentDataId = documentDataId;
		this.documentTitle = documentTitle;
		this.docDataDescription = docDataDescription;
		this.createUser = createUser;
		this.documentCreationTs = documentCreationTs;
		this.lastModUser = lastModUser;
		this.documentModTs = documentModTs;
		this.category = category;
	}

	public String getDocumentDataId() {
		return documentDataId;
	}

	public void setDocumentDataId(String documentDataId) {
		this.documentDataId = documentDataId;
	}

	public String getDocumentTitle() {
		return documentTitle;
	}

	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}

	public String getDocDataDescription() {
		return docDataDescription;
	}

	public void setDocDataDescription(String docDataDescription) {
		this.docDataDescription = docDataDescription;
	}

	public UserEntity getCreateUser() {
		return createUser;
	}

	public void setCreateUser(UserEntity createUser) {
		this.createUser = createUser;
	}

	public Date getDocumentCreationTs() {
		return documentCreationTs;
	}

	public void setDocumentCreationTs(Date documentCreationTs) {
		this.documentCreationTs = documentCreationTs;
	}

	public UserEntity getLastModUser() {
		return lastModUser;
	}

	public void setLastModUser(UserEntity lastModUser) {
		this.lastModUser = lastModUser;
	}

	public Date getDocumentModTs() {
		return documentModTs;
	}

	public void setDocumentModTs(Date documentModTs) {
		this.documentModTs = documentModTs;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

	public Set<DocumentPartEntity> getParts() {
		return parts;
	}

	public void setParts(Set<DocumentPartEntity> parts) {
		this.parts = parts;
	}

	public String getRootElementType() {
		return rootElementType;
	}

	public void setRootElementType(String rootElementType) {
		this.rootElementType = rootElementType;
	}

}

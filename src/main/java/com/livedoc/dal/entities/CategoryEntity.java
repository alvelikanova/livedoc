package com.livedoc.dal.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "category", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {
		"category_name", "category_project_id" }))
public class CategoryEntity extends BaseDalEntity {

	private static final long serialVersionUID = -7585918596502377748L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "category_id", unique = true, nullable = false, length = 32)
	private String categoryId;

	@Column(name = "category_name", unique = true, nullable = false, length = 64)
	private String categoryName;

	@Column(name = "category_description", length = 256)
	private String categoryDescription;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_project_id", nullable = false)
	private ProjectEntity project;

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "category")
	@Cascade({ CascadeType.ALL })
	private Set<DocumentDataEntity> documentDataList = new HashSet<DocumentDataEntity>(
			0);

	public CategoryEntity() {
		super();
	}

	public CategoryEntity(String categoryId, String categoryName,
			String categoryDescription, ProjectEntity project,
			Set<DocumentDataEntity> documentDataList) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryDescription = categoryDescription;
		this.project = project;
		this.documentDataList = documentDataList;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public ProjectEntity getProject() {
		return project;
	}

	public void setProject(ProjectEntity project) {
		this.project = project;
	}

	public Set<DocumentDataEntity> getDocumentDataList() {
		return documentDataList;
	}

	public void setDocumentDataList(Set<DocumentDataEntity> documentDataList) {
		this.documentDataList = documentDataList;
	}

}

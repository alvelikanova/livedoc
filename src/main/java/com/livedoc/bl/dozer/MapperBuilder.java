package com.livedoc.bl.dozer;

import org.dozer.loader.api.BeanMappingBuilder;

import com.livedoc.bl.domain.entities.Category;
import com.livedoc.bl.domain.entities.Comment;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.domain.entities.DocumentPart;
import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.domain.entities.Role;
import com.livedoc.bl.domain.entities.User;
import com.livedoc.dal.entities.CategoryEntity;
import com.livedoc.dal.entities.CommentEntity;
import com.livedoc.dal.entities.DocumentDataEntity;
import com.livedoc.dal.entities.DocumentPartEntity;
import com.livedoc.dal.entities.ProjectEntity;
import com.livedoc.dal.entities.RoleEntity;
import com.livedoc.dal.entities.UserEntity;

public class MapperBuilder extends BeanMappingBuilder {

	@Override
	protected void configure() {
		// TODO collections are mapped by name - ???
		mapping(UserEntity.class, User.class).fields("userId", "id")
				.fields("username", "name").fields("password", "password");
		mapping(RoleEntity.class, Role.class).fields("roleId", "id")
				.fields("roleName", "name").fields("roleCode", "code");
		mapping(ProjectEntity.class, Project.class).fields("projectId", "id")
				.fields("projectName", "name")
				.fields("projectDescription", "description");
		mapping(DocumentDataEntity.class, DocumentData.class)
				.fields("documentDataId", "id")
				.fields("documentTitle", "title")
				.fields("documentCreationTs", "createDate")
				.fields("docDataDescription", "description")
				.fields("documentModTs", "lastModDate")
				.fields("rootElementType", "rootElement");
		mapping(CategoryEntity.class, Category.class)
				.fields("categoryId", "id").fields("categoryName", "name")
				.fields("categoryDescription", "description");
		mapping(DocumentPartEntity.class, DocumentPart.class)
				.fields("documentPartId", "id")
				.fields("documentPartOrder", "order")
				.fields("documentPartContent", "content")
				.fields("docPartRootElemType", "rootElementType");
		mapping(CommentEntity.class, Comment.class).fields("commentId", "id")
				.fields("commentSubmitTs", "submitDate")
				.fields("commentContent", "comment");
	}
}
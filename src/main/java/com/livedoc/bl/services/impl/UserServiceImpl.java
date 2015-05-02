package com.livedoc.bl.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.livedoc.bl.domain.entities.Project;
import com.livedoc.bl.domain.entities.User;
import com.livedoc.bl.services.UserService;
import com.livedoc.dal.entities.ProjectEntity;
import com.livedoc.dal.entities.UserEntity;
import com.livedoc.dal.providers.UserDataProvider;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDataProvider userDataProvider;
	@Autowired
	private DozerBeanMapper mapper;

	public User findUserByLoginName(String loginName) {
		UserEntity usersEntity = userDataProvider.findByLoginName(loginName);
		if (usersEntity != null) {
			User user = mapper.map(usersEntity, User.class);
			// map project and add to business entity
			return user;
		}
		return null;
	}

	public User findUserById(String id) {
		UserEntity usersEntity = userDataProvider.findById(id);
		if (usersEntity != null) {
			User user = mapper.map(usersEntity, User.class);
			Set<ProjectEntity> projectEntities = usersEntity.getProjects();
			List<Project> projects = new ArrayList<Project>();
			for (ProjectEntity projectEntity : projectEntities) {
				Project project = mapper.map(projectEntity, Project.class);
				projects.add(project);
			}
			user.setProjects(projects);
			return user;
		}
		return null;
	}

	public List<User> findAllUsers() {
		List<User> users = new ArrayList<User>();
		List<UserEntity> usersEntities = userDataProvider.findAll();
		for (UserEntity usersEntity : usersEntities) {
			User user = mapper.map(usersEntity, User.class);
			users.add(user);
		}
		return users;
	}

	public User saveUser(User user) {
		UserEntity userEntity = mapper.map(user, UserEntity.class);
		userDataProvider.saveOrUpdate(userEntity);
		return mapper.map(userEntity, User.class);

	}

	public void deleteUser(User user) {
		UserEntity userEntity = mapper.map(user, UserEntity.class);
		userDataProvider.delete(userEntity);
	}
}

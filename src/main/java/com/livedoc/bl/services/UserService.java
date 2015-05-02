package com.livedoc.bl.services;

import java.util.List;

import com.livedoc.bl.domain.entities.User;

public interface UserService {

	User findUserByLoginName(String loginName);

	User findUserById(String id);

	List<User> findAllUsers();

	User saveUser(User user);

	void deleteUser(User user);
}

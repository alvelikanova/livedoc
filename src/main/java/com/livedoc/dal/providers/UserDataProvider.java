package com.livedoc.dal.providers;

import java.util.List;

import com.livedoc.dal.entities.UserEntity;

public interface UserDataProvider extends
		GenericDataProvider<UserEntity, String> {
	
	UserEntity findByLoginName(String loginName);

	List<UserEntity> findUsers(List<String> excludeUsersIds);
	
}

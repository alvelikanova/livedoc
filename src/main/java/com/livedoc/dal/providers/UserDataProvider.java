package com.livedoc.dal.providers;

import com.livedoc.dal.entities.UserEntity;

public interface UserDataProvider extends GenericDataProvider<UserEntity, String> {
	UserEntity findByLoginName(String loginName);

}

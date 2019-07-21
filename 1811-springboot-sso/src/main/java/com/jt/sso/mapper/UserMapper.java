package com.jt.sso.mapper;

import com.jt.common.pojo.User;

public interface UserMapper {

	int checkUsername(String userName);

	int saveUser(User user);

	User login(User user);

}

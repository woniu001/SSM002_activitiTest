package com.wbliu.SSM002.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wbliu.SSM002.beans.UserBean;
import com.wbliu.SSM002.mapper.UserMapper;
import com.wbliu.SSM002.service.ILoginService;
@Service
public class LoginServiceImpl implements ILoginService{
   /* 
    @Resource
    private UserMapper um;


    @Override
    public UserBean Login(String username, String password) {
     
    	
    	UserBean u = new UserBean();
    	u.setUsername(username);
    	u.setPassword(password);
    	
    	return um.login(u);
    	return um.login(username, password);
    }
*/
}
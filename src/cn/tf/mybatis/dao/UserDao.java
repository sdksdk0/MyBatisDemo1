package cn.tf.mybatis.dao;

import java.util.List;

import cn.tf.mybatis.po.User;

public interface  UserDao {
	
	public User findUserById(int id) throws Exception;
	
	
	public List<User>  findUserByName(String name) throws Exception;
	
	public void insertUser(User user) throws Exception;
	
	public void updateUser(User user) throws Exception ;
	
	
	
	
}

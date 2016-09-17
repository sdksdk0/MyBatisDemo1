package cn.tf.mybatis.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cn.tf.mybatis.dao.UserDao;
import cn.tf.mybatis.po.User;

public class UserDaoImpl implements  UserDao{

	private SqlSessionFactory  sqlSessionFactory;
	public UserDaoImpl(SqlSessionFactory  sqlSessionFactory){
		this.sqlSessionFactory=sqlSessionFactory;
	}
	
	
	@Override
	public User findUserById(int id) throws Exception {
		SqlSession  sqlSession=null;
		User user = null;
		try{
			sqlSession = sqlSessionFactory.openSession();
			user = sqlSession.selectOne("test.findUserById", 1);

			System.out.println(user);

		} catch (Exception e) {
			
		}finally{
			if(sqlSession!=null){
				sqlSession.close();
			}
		}
		return user;
	}


	@Override
	public List<User> findUserByName(String name) throws Exception {
		SqlSession  sqlSession=null;
		List<User> list = null;
		try{
			sqlSession = sqlSessionFactory.openSession();
			list = sqlSession.selectList("test.findUserByName", name);

			System.out.println(list);

		} catch (Exception e) {
			
		}finally{
			if(sqlSession!=null){
				sqlSession.close();
			}
		}
		return list;
	}


	@Override
	public void insertUser(User user) throws Exception {
		SqlSession  sqlSession=null;
		
		try{
			sqlSession = sqlSessionFactory.openSession();
			
			 sqlSession.insert("test.insertUser", user);

			System.out.println(user.getId());
			sqlSession.commit();
		} catch (Exception e) {
			
		}finally{
			if(sqlSession!=null){
				sqlSession.close();
			}
		}

	}


	@Override
	public void updateUser(User user) throws Exception {
		
		SqlSession  sqlSession=null;
	
		try{
			sqlSession = sqlSessionFactory.openSession();
			
			 sqlSession.update("test.updateUser", user);

			System.out.println(user.getId());
			sqlSession.commit();
		} catch (Exception e) {
			
		}finally{
			if(sqlSession!=null){
				sqlSession.close();
			}
		}
	}
	
	

}

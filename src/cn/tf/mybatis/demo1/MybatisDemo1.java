package cn.tf.mybatis.demo1;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import cn.tf.mybatis.po.User;

public class MybatisDemo1 {
	
	
	@Test
	public void findUserById() throws IOException{
		
		String resource="SqlMapConfig.xml";
		//创建配置文件的流
		InputStream inputStream=Resources.getResourceAsStream(resource);
		//根据配置文件创建sqlSessionFactory
		SqlSessionFactory  sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession=sqlSessionFactory.openSession();
		User user=sqlSession.selectOne("test.findUserById",1);
		System.out.println(user);
		
		//关闭
		sqlSession.close();
	}
	
	@Test
	public void findUserByName() throws IOException{
		
		String resource="SqlMapConfig.xml";
		//创建配置文件的流
		InputStream inputStream=Resources.getResourceAsStream(resource);
		//根据配置文件创建sqlSessionFactory
		SqlSessionFactory  sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession=sqlSessionFactory.openSession();
		List<User> list=sqlSession.selectList("test.findUserByName","张");
		System.out.println(list);
		
		//关闭
		sqlSession.close();
	}
	
	
	//@Test
	public void insertUser() throws IOException{
		
		String resource="SqlMapConfig.xml";
		//创建配置文件的流
		InputStream inputStream=Resources.getResourceAsStream(resource);
		//根据配置文件创建sqlSessionFactory
		SqlSessionFactory  sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession=sqlSessionFactory.openSession();
		
		User user=new User();
		user.setUsername("王大锤");
		user.setAddress("湖南衡阳");
		
		
		sqlSession.insert("test.insertUser",user);
	
		sqlSession.commit();
		//关闭
		sqlSession.close();
	}
	
	
	//@Test
	public void deleteUser() throws IOException{
		
		String resource="SqlMapConfig.xml";
		//创建配置文件的流
		InputStream inputStream=Resources.getResourceAsStream(resource);
		//根据配置文件创建sqlSessionFactory
		SqlSessionFactory  sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession=sqlSessionFactory.openSession();
		
		
		sqlSession.delete("test.deleteUser",27);
	
		sqlSession.commit();
		//关闭
		sqlSession.close();
	}
	
	@Test
	public void updateUser() throws IOException{
		
		String resource="SqlMapConfig.xml";
		//创建配置文件的流
		InputStream inputStream=Resources.getResourceAsStream(resource);
		//根据配置文件创建sqlSessionFactory
		SqlSessionFactory  sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession=sqlSessionFactory.openSession();
		
		User user=new User();
		user.setId(1);
		user.setUsername("王大锤");
		user.setAddress("湖南衡阳");
		
		
		sqlSession.update("test.updateUser",user);
	
		sqlSession.commit();
		//关闭
		sqlSession.close();
	}
	

}

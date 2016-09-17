package cn.tf.mybatis.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import cn.tf.mybatis.dao.UserDao;
import cn.tf.mybatis.dao.impl.UserDaoImpl;
import cn.tf.mybatis.po.User;

public class UserDaoImplTest {
	private SqlSessionFactory  sqlSessionFactory;
	
	
	@Before
	public void setUp() throws IOException{
		String resource="SqlMapConfig.xml";
		//创建配置文件的流
		InputStream inputStream=Resources.getResourceAsStream(resource);
		//根据配置文件创建sqlSessionFactory
		sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		
	}
	
	
	
	
	@Test
	public void test1() throws Exception{
		//调用dao对象
		UserDao userDao=new UserDaoImpl(sqlSessionFactory);
		
		User user=userDao.findUserById(1);
		System.out.println(user);
		
	}

}

package cn.tf.mybatis.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import cn.tf.mybatis.mapper.OrderMapper;
import cn.tf.mybatis.mapper.UserMapper;
import cn.tf.mybatis.po.Orders;
import cn.tf.mybatis.po.OrdersCustom;
import cn.tf.mybatis.po.QueryOrderVo;
import cn.tf.mybatis.po.User;

public class CacheTest {

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
	public void test1() throws Exception {
		SqlSession sqlSessio=sqlSessionFactory.openSession();
	
		UserMapper mapper=sqlSessio.getMapper(UserMapper.class);
		User user=mapper.findUserById(1);
		System.out.println(user);
			
		user=mapper.findUserById(1);
		System.out.println(user);
		
		sqlSessio.close();
	}
	
	
	@Test
	public void test2() throws Exception {
SqlSession sqlSession1 = sqlSessionFactory.openSession();
		
		SqlSession sqlSession2 = sqlSessionFactory.openSession();
		
		SqlSession sqlSession3 = sqlSessionFactory.openSession();
		//代理对象
		UserMapper mapper1 = sqlSession1.getMapper(UserMapper.class);
		
		UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);
		
		UserMapper mapper3 = sqlSession3.getMapper(UserMapper.class);
		
		
		//第一次查询根据用户id查询用户信息
		User user1 = mapper1.findUserById(1);
		System.out.println(user1);
		
		//注意：将sqlSession1.close时将数据存入二级缓存
		sqlSession1.close();
		
		//使用sqlSession3进行用户添加、修改、删除执行commit，会清空二级缓存
		//.....
		
		//第二次查询根据用户id查询用户信息
		User user2 = mapper2.findUserById(1);
		System.out.println(user2);
		
		sqlSession2.close();
	}
	
	
}

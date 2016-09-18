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
import cn.tf.mybatis.po.Orders;
import cn.tf.mybatis.po.OrdersCustom;
import cn.tf.mybatis.po.QueryOrderVo;
import cn.tf.mybatis.po.User;

public class OrderMapperTest {

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
	public void test() throws Exception {
		SqlSession sqlSessio=sqlSessionFactory.openSession();
	
		OrderMapper  orderMapper=sqlSessio.getMapper(OrderMapper.class);
		
		QueryOrderVo   queryOrderVo=new QueryOrderVo();
		Orders orders=new Orders();
		orders.setId(3);
		
		queryOrderVo.setOrders(orders);
		List<Integer> ids=new ArrayList<Integer>();
		ids.add(3);
		ids.add(4);
		queryOrderVo.setIds(ids);
		
		List<Orders> list=orderMapper.findOrders(queryOrderVo);
		System.out.println(list);
		sqlSessio.close();
	}
	
	@Test
	public void test2() throws Exception {
		SqlSession sqlSessio=sqlSessionFactory.openSession();
	
		OrderMapper  orderMapper=sqlSessio.getMapper(OrderMapper.class);
		
		
		List<OrdersCustom> list=orderMapper.findOrderAndUserList();
		System.out.println(list);
		sqlSessio.close();
	}
	
	@Test
	public void test3() throws Exception {
		SqlSession sqlSessio=sqlSessionFactory.openSession();
	
		OrderMapper  orderMapper=sqlSessio.getMapper(OrderMapper.class);
		
		
		List<Orders> list=orderMapper.findOrderAndListResultMap();
		System.out.println(list);
		sqlSessio.close();
	}
	
	@Test
	public void test4() throws Exception {
		SqlSession sqlSessio=sqlSessionFactory.openSession();
	
		OrderMapper  orderMapper=sqlSessio.getMapper(OrderMapper.class);
		
		List<Orders> list=orderMapper.findOrderAndDetailList();
		System.out.println(list);
		sqlSessio.close();
	}
	
	@Test
	public void test5() throws Exception {
		SqlSession sqlSessio=sqlSessionFactory.openSession();
	
		OrderMapper  orderMapper=sqlSessio.getMapper(OrderMapper.class);
		
		List<User> list=orderMapper.findUserAndItemList();
		System.out.println(list);
		sqlSessio.close();
	}
	
	
	@Test
	public void test6() throws Exception {
		SqlSession sqlSessio=sqlSessionFactory.openSession();
	
		OrderMapper  orderMapper=sqlSessio.getMapper(OrderMapper.class);
		
		List<Orders> list=orderMapper.findOrderUserLazyLoad();
		
		Orders orders=list.get(0);
		User user=orders.getUser();
		System.out.println(user);
		sqlSessio.close();
	}
}

package cn.tf.mybatis.mapper;

import java.util.List;

import cn.tf.mybatis.po.Orders;
import cn.tf.mybatis.po.OrdersCustom;
import cn.tf.mybatis.po.QueryOrderVo;
import cn.tf.mybatis.po.User;

public interface OrderMapper {
	
	public List<Orders>   findOrders(QueryOrderVo  queryOrderVo)  throws Exception;

	public List<OrdersCustom>  findOrderAndUserList() ;
	
	public List<Orders> findOrderAndListResultMap();
	
	
	public List<Orders>  findOrderAndDetailList();
	
	
	public List<User>  findUserAndItemList();
	
	public List<Orders>  findOrderUserLazyLoad();
	
	
}

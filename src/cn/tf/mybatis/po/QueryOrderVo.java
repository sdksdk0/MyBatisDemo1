package cn.tf.mybatis.po;

import java.util.List;

//订单查询的包装对象
public class QueryOrderVo {
	
	private List<Integer>  ids;
	private User user;
	private Orders orders;
	
	
	
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	
	
	

}

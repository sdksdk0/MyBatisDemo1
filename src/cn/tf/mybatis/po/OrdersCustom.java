package cn.tf.mybatis.po;


//对扩展对象，可以采用继承方式 ，让他继承拥有字段多的pojo
public class OrdersCustom extends Orders {
	
	
	//包括 用户信息
	private String username;
	private String sex;
	private String address;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	

}

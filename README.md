# MyBatisDemo1
myBatis基础，原始Dao开发，mapper代理，输入输出映射等！

#简介
是一个持久层框架，前身是ibatis，对使用jdbc操作数据库进行了封装。通过xml或注解的方式将要执行的各种statement配置起来。

SqlMapConfig.xml:mybatis全局配置文件，名称不固定。
一般来说用表名mapping.xml。


#开发步骤
1、导入相关jar包
2、配置sqlMapConfig.xml

	<properties resource="db.properties">
		
	</properties>
	
	<!-- mybatis运行环境配置 -->
	<environments default="development">
		<environment id="development">
		<!-- 使用jdbc事务管理-->
			<transactionManager type="JDBC" />
		<!-- mybatis自身的数据库连接池-->
			<dataSource type="POOLED">
				<property name="driver" value="${jdbc.driver}" />
				<property name="url" value="${jdbc.url}" />
				<property name="username" value="${jdbc.username}" />
				<property name="password" value="${jdbc.password}" />
			</dataSource>
		</environment>
	</environments>
	
	<mappers>
		<mapper resource="sqlmap/User.xml"/>
	</mappers>

3、配置User.xml

	<select id="findUserById"   parameterType="int"   resultType="cn.tf.mybatis.po.User">
			select * from user where id=#{id}
		</select>

 id:用于唯一标识一个sql语句     
			paramterType:设置输入参数的类型 
			resultType:指定输出结果映射的类型，单条记录所映射的类型,需要指定pojo的全路径
			#{}表示占位符,#{id}表示接收输入参数id，如果输入参数是简单类型，#{}中间可以使用任意名称
		

4、创建po

	private int id;
	private String username;// 用户姓名
	private String sex;// 性别
	private Date birthday;// 生日
	private String address;// 地址

给定getter、setter方法


5、调用

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


根据用户名称查询：

 	    	`<select id="findUserByName"  parameterType="java.lang.String"  resultType="cn.tf.mybatis.po.User" >`
    			sselect * from user where username like '%${value}%'select * from user where username like '%${value}%'
    		</select>


测试中：

    List<User> list=sqlSession.selectList("test.findUserByName","张");


##增删改

	<!-- 插入数据 
			#{}:接收输入参数，使用OGNL解析对象中的属性，表示方法就是对象.属性
			#{username}表示解析到输入参数的user对象的username属性值
		-->
		<insert id="insertUser"    parameterType="cn.tf.mybatis.po.User">
			insert into user(username,birthday,sex,address)  values(#{username},#{birthday},#{sex},#{address})
		</insert>
		
		<!-- 删除用户 -->
		<delete id="deleteUser"  parameterType="int">
			delete from user where id=#{value}
		</delete>
		
		<!-- 更新 -->
		<update id="updateUser"  parameterType="cn.tf.mybatis.po.User">
			update user set username=#{username},birthday=#{birthday},sex=#{sex},address=#{address}  where id=#{id}
		</update>


测试中：

		User user=new User();
		user.setUsername("王大锤");
		user.setAddress("湖南衡阳");
		
		
		sqlSession.insert("test.insertUser",user);

    sqlSession.delete("test.deleteUser",27);



	User user=new User();
		user.setId(1);
		user.setUsername("王大锤");
		user.setAddress("湖南衡阳");
		
		
		sqlSession.update("test.updateUser",user);

insert中主键设置：

		<insert id="insertUser"    parameterType="cn.tf.mybatis.po.User">
			<selectKey  order="AFTER"  keyProperty="id"   resultType="int">
					select last_insert_id()
			</selectKey>
			
			insert into user(username,birthday,sex,address)  values(#{username},#{birthday},#{sex},#{address})
		</insert>

uuid:

	<selectKey  order="BEFORE"  keyProperty="id"   resultType="java.lang.String">
					select uuid() 
			</selectKey>

##小结

parameterType：指定输入映射参数的类型，可以是基本类型、pojo。。。
resultType：指定输出结果映射类型，确切的理解是指定单条记录所映射的类型，可以是pojo、基本类型、hashmap..

    #{}：表示一个占位符号，不需要程序员去关注参数内容或类型，mybatis自动进行参数映射，底层使用预编译。
    
${}：表示一个sql拼接符号，${value}表示接收输入 参数将接收到参数内容不加任何修饰拼接在sql语句中，缺点是无法避免sql注入。底层不使用预编译。

    #{}和${}使用OGNL从输入参数中获取参数值，表达式为：属性.属性.属性....

建议使用#{}，有时候必须使用${}，例如：动态拼接表名组成sql语句：
select * from ${tablename}
再比如将排序字段动态拼接，也必须使用${}：
select * from user ordre by ${fieldname}


selectOne方法：只查询返回单条记录的sql，如果返回多条记录，使用selectOne抛出异常
能用selectOne就可以使用selectList（List只有一个对象）

selectList方法：将多条记录映射成List<pojo>对象,


#myBatis开发Dao层的方法
sqlSession是一个接口，面向程序员的接口，其实现类是线程不安全的。


##原始开发方式
接口：

    public interface  UserDao {
	
		public User findUserById(int id) throws Exception;
	
	
	public List<User>  findUserByName(String name) throws Exception;
	
	public void insertUser(User user) throws Exception;
	
	public void updateUser(User user) throws Exception ;
	
	
    }



接口的实现：

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


从这里我们可以看出，原始的方法代码太过于冗余，代码执行流程相同，存在硬编码，所以我们需要想个办法来简化代码。

解决以上问题的开发规范：


将mapper方法和mapper.xml中的statement进行一对一关联。

将mapper映射文件和dao接口关联。

将dao方法输入参数类型和statement中的parameterType保持一致。

将dao方法返回值类型和statement中的resultType保持一致。

##mapper代理开发
1、命名空间要关联mapper接口

    <mapper  namespace="cn.tf.mybatis.mapper.UserMapper">
    
    </mapper>

2、将mapper接口方法名和statement的id一致

    <select id="findUserById"   parameterType="int"   resultType="cn.tf.mybatis.po.User">
    			select * from user where id=#{id}
    		</select>
3、返回值类型一致

如果dao返回的是List<pojo>,pojo的类型和resultType保持一致，如果dao方法返回pojo，resultType保持一致。


这样写的话实现类就不需要写了。

##小结

mapper属于持久层接口，持久层是给各各service接口服务，所以这里可以将多个参数（如果有多个参数）封装到一个包装对象，其实不影响系统可扩展性。
包装对象：自定义pojo，pojo属性也是pojo，即pojo将各各参数pojo组合起来。

如果mapper方法返回值为pojo单个对象，代理对象内部使用selectOne调用。
如果mapper方法返回值为List集合对象，代理对象内部使用selectList调用


#SqlMapConfig.xml
全局配置文件，需要配置的内容为：

- properties（属性）
- settings（全局配置参数）
- typeAliases（类型别名）
- typeHandlers（类型处理器）
- objectFactory（对象工厂）
- plugins（插件）
- environments（环境集合属性对象）
- environment（环境子属性对象）
- transactionManager（事务管理）
- dataSource（数据源）
- mappers（映射器）



设置别名：

	<typeAliases>
		<typeAlias type="cn.tf.mybatis.po.User"    alias="user"/>
	</typeAliases>


通过加载mapper接口 

mapper.xml和mapper.java同名且在同一个目录中

		<mapper class="cn.tf.mybatis.mapper.UserMapper" />

批量加载mapper接口 

		<package name="cn.tf.mybatis.mapper"/>

##输入映射
paramterType:指定输入参数类型，包括：基本类型、pojo、hashmap

###输入包装类型对象
根据综合条件进行用户信息查询，

    <select id="findUserListByResultMap" parameterType="queryUserVo" resultMap="userListResultMap">
		select id id_,username username_,birthday birthday_ from user where username like '%${user.username}%'
	</select>



##输出映射


 id：指定结果集唯一标识，如果有多列决定唯一定义多个id
	    <id/>
	 	<id/>
	 	
	 	column：sql查询结果集的列名
	 	property：要将列映射到pojo的哪个属性中


##动态sql
	<sql id="query_user_where">
			<!-- where:
		相当 sql中where关键字，可以将第一个条件前边的and去除
		 -->
		
			<if test="user!=null">
			<if test="user.username!=null and user.username!=''">
				and username like '%${user.username}%' 
			</if> 
		</if>
		<if test="user!=null">
			<if test="user.id!=null and user.id!=''">
				and id=#{user.id}
			</if>
		</if>
	
	</sql>


###foreach
输入参数中如果有list、数组等可以使用foreach遍历集合对象，从而动态拼接sql。

collection：接收paramterType的参数 ，
open：开始遍历第一个拼接的sql，
close:结束遍历拼接的sql，
separator:两次拼接的连接
		

			<foreach collection="ids"  open="and ("   close=")"  item="id"   separator="or">
				id=#{id}
			
			</foreach>

#高级映射

##一对一

    <resultMap type="orders" id="OrderAndListResultMap">
			<id  column="id"  property="id"/>   <!-- 订单信息的唯一标识 -->
			<result   column="user_id"   property="userId"/>  
			<result  column="number"  property="number"/>
			<result  column="createtime"  property="createtime"/>
			<result column="note" property="note"/>
			
			<!-- 一对一关联查询 -->
			<association property="user"  javaType="cn.tf.mybatis.po.User"  >
					<id  column="user_id"   property="id"/>
					<result  column="username"  property="username"/>
					<result  column="sex"  property="sex"/>
					<result  column="address" property="address"/>
			</association>
		</resultMap>
		
		
		<select id="findOrderAndListResultMap"  resultMap="OrderAndListResultMap">
				SELECT orders.*,username,sex,address  FROM orders,USER  WHERE orders.`user_id`=user.`id`
		</select>


如果没有对结果集的特殊映射要求，则在实际开发中使用resultType进行一对一查询更好。

例如将关联查询信息映射到pojo属性中。


##一对多
		<resultMap type="orders" id="findOrderAndDetailList"   extends="OrderAndListResultMap">
			<!-- 订单信息映射,继承了 -->
			
			<!-- 订单明细映射
				property:将明细信息映射到orders的那个属性
			 -->
			<collection property="orderdetails"   ofType="cn.tf.mybatis.po.Orderdetail">
					<id column="orderdetail_id"  property="id"  />
					<result   column="items_num"  property="itemsNum"/>
					<result   column="items_id"  property="itemsId" />
			</collection>
		
		</resultMap>


###延迟加载
当进行复杂的关联查询时，如果只查询主要的内容不查询关联信息就能满足用户需要的时候，可以只查出主要内容返回，当需要查询关联信息时，再去查关联信息。优点在于：提高数据库查询性能。

	<!-- 用户信息延迟加载   select:延迟加载的statement的id-->
				<association property="user"   select="cn.tf.mybatis.mapper.UserMapper.findUserById"   column="user_id">
						
				
				</association>



延迟加载配置

	<settings>
		<!-- 延迟加载配置 -->
		<!-- 打开延迟加载开关 -->
		<setting name="lazyLoadingEnabled" value="true"/>
		<!-- 设置按需加载 -->
		<setting name="aggressiveLazyLoading" value="false"/>
		
	</settings>

#查询缓存


一级缓存：是线程级别的缓存，不同的sqlSession的一级缓存map不能共用，当sqlSession关闭或者commit，该sqlSession的一级缓存数据不清空。

二级缓存：是跨进程级别的缓存，不同的sqlSession的二级缓存的map可以共用，二级缓存是以mapper为单位创建map数据结构，有几个map就有几个mapper。

myBatis默认支持一级缓存。
打开二级缓存：

    <setting name="cacheEnabled" value="true"/>


当二级缓存开启，先去二级缓存中查找，如果二级缓存没有，再去一级缓存查找。




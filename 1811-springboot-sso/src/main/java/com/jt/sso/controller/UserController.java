package com.jt.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.common.config.RedisService;
import com.jt.common.pojo.User;
import com.jt.common.util.MD5Util;
import com.jt.common.util.OUtil;
import com.jt.common.util.UUIDUtil;
import com.jt.common.vo.SysResult;
import com.jt.sso.mapper.UserMapper;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@RestController //所有的方法的返回值都不做页面解析,响应体的返回
@RequestMapping("user")//当前类中的所有requestMapping地址都拼接前缀user
public class UserController {
	
	@Autowired
	private UserMapper userMapper;
	
	/*
	请求地址	sso.jt.com/user/check/{userName}
	请求参数	路径传参 String userName
	
	请求方式	get
	备注	查询一个存在不存在的userName,返回1存在/0不存在
	返回数据	第一种:1/0
	第二种:SysResut不仅能返回功能需要的结果,还能返回状态,信息,其他数据;
	 */
	@RequestMapping("check/{userName}")
	public String checkUserName(@PathVariable String userName){
		return userMapper.checkUsername(userName)+"";
	}
	
	/*
	 请求连接地址	sso.jt.com/user/register
	 请求方式	post
	 请求参数	User user对象 缺少type和userId；密码加密
	 备注	uuid添加,type=0,password需要md5加密;
	 insert into t_user
	 返回数据	第一种:1/0;1表示新增成功,0表示失败
	 第二种:SysResut不仅能返回功能需要的结果,还能返回状态,信息,其他数据;
	 */
	@RequestMapping("register")
	public String doRegister(User user){
		//补充内容
		user.setUserType(0);
		user.setUserId(UUIDUtil.getUUID());
		//加密
		user.setUserPassword(MD5Util.md5(user.getUserPassword()));
		return userMapper.saveUser(user)+"";
	}
	
	//登录逻辑接口方法1
	/*
	 请求地址	sso.jt.com/user/login
	 请求参数	String userName,String userPassword-->User user
		
	 请求方式	post
	 备注	select * from t_user where user_name=参数1
		and user_password=参数2（密码加密字符串）;返回不是null就是一个user对象
	 返回数据	"e10adc3949ba59abbe56e057f20f883e"
	 备注	返回数据是存储在redis的key叫做ticket
		计算方法=“JT_TICKET”+currentTime+userName的md5加密
	 */
	@Autowired
	private RedisService redis;
	@Autowired
	private ShardedJedisPool pool;
	@RequestMapping("login")
	public String doLogin(User user){
		//user对象中只有username和password的属性,passwrod是明文
		//验证合法,使用mapper查询where user_name=arg1,user_password=arg2
		ShardedJedis jedis = pool.getResource();
		user.setUserPassword(MD5Util.md5(user.getUserPassword()));
		User _user = userMapper.login(user);
		try{
			if(_user!=null){//说明有值,登录信息正确
				//将数据存储到redis,第三方存储
				//准备key ticket 算法=MD5("JT_TICKET"+TIME+USERNAME)
				String ticket=MD5Util.md5("JT_TICKET"+
						System.currentTimeMillis()+user.getUserName());
				//表示同一个用户,在不同时间登录时在redis的key值
				String userJson=OUtil.mapper.writeValueAsString(_user);
				//存储
				//redis.set(ticket, userJson);//登录超时30分钟
				//删除有的ticket/实现一个用户只能登陆一次
				jedis.del(jedis.get(_user.getUserId()));
				//set当前登录的ticket
				jedis.set(_user.getUserId(), ticket);
				
				jedis.set(ticket,userJson);
				jedis.expire(ticket, 20);
				return ticket;
			}
			return "";//说明验证失败,用户名密码不对;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}finally{
			pool.returnResource(jedis);
		}
		
	}
	
	//登录接口第二个方法,利用前台传递的ticket,验证redis数据
	/*
	请求地址	sso.jt.com/user/query/{ticket}
	请求方式	get（返回jsonp）
	请求参数	String ticket String callback；
	备注	如果callback==null，说明请求的人不是jsonp，如果callback!=null，说明请求的是jsonp格式数据，
	返回数据	SysResult对象的json/或者JSONP数据格式;data携带userJson
	备注	if callback==null 直接将SysResult的json返回，其中 status表示成功失败1/0，data封装userJson数据(成功时封装)
		else callback!=null 
		        将sysResult对象封装到jsonp格式
		        callback（sysResult的json）
	 */
	@RequestMapping("query/{ticket}")
	public String checkLogin(@PathVariable String ticket,
			String callback){
		//获取了前台传递的ticket,用户需要用ticket验证是否登录,验证
		//redis是否已经存在user数据,有就是登录过了,没有就是没登录
		
		//String userJson = redis.get(ticket);
		//根据userJson判断是否登录,将userJson(""/{"":"","":"","":""})
		//sysResult的data属性,赋值userJson
		//判断超时剩余时间
		ShardedJedis jedis = pool.getResource();
		Long timeOT = jedis.ttl(ticket);//剩余秒数
		if(timeOT<60*5){//用户已经连续访问了大于25分钟系统
			jedis.expire(ticket, (int)(timeOT+(60*10)));
			//存在瞬间超时，无法执行续约
			//一定是用户至少5分钟没有使用用户操作系统;
		}
		String userJson=jedis.get(ticket);//超时了返回空,续约失败返回空,续约成功返回userJson,没达到续约条件,返回userJson
		
		
		String jsonData="";
		try{
			jsonData=OUtil.mapper.
			writeValueAsString(SysResult.oK(userJson));
		}catch(Exception e){
			return jsonData;
		}	
		//{"status":"200","msg":"ok","data":{"userId":"","userName"}}
		//根据callback返回数据结构,非空的callback表示jsonp格式
		//空callback表示json格式
		if(callback==null){//说明拦截器发起请求,返回userJson
			if(userJson==null){
				return "";
			}
			return userJson;
		}else{//非空,需要返回jsonp格式
			String result=callback+"("+jsonData+")";
			return result;
		}
	}
	
	
	
	
	
	
}

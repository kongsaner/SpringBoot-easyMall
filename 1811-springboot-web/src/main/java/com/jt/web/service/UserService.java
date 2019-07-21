package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.common.config.HttpClientService;
import com.jt.common.pojo.User;
import com.jt.common.vo.HttpResult;
import com.jt.common.vo.SysResult;

@Service
public class UserService {
	@Autowired
	private HttpClientService client;
	public SysResult checkUsername(String userName) {
		//按照接口文件,发送数据给sso,
		String url="http://sso.jt.com/user/check/"
				+userName;
		try{//没有额外参数,直接get请求
			String exists=client.doGet(url);//1/0
			int status=Integer.parseInt(exists);
			return SysResult.build(status, "查询完毕");
		}catch(Exception e){
			//异常表示查询失败
			return SysResult.build(1, e.getMessage());
		}
	}
	public int saveUser(User user) throws Exception {
		//将user封装为httpclient的参数
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userName", user.getUserName());
		map.put("userEmail", user.getUserEmail());
		map.put("userPassword", user.getUserPassword());
		map.put("userNickname", user.getUserNickname());
		//post地址
		String url="http://sso.jt.com/user/register";
		HttpResult result = client.doPost(url,map);
		//获取body的值
		int success =Integer.parseInt(result.getBody());
		return success;
	}
	public String doLoin(User user){
		//按照接口文件,发送数据,接收数据
		String url="http://sso.jt.com/user/login";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userName", user.getUserName());
		map.put("userPassword", user.getUserPassword());
		try{
			//httpResult的body就是sso返回的ticket,redis的key值
			String ticket=client.doPost(url, map).getBody();
			return ticket;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		
	}

}

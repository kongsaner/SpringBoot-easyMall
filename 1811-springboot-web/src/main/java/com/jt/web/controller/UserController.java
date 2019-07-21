package com.jt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.pojo.User;
import com.jt.common.util.CookieUtils;
import com.jt.common.vo.SysResult;
import com.jt.web.service.UserService;


@Controller
public class UserController {
	@Autowired
	private UserService userService;
	//完成username校验,是否存在
	@RequestMapping("user_ajax/checkUserName")
	@ResponseBody
	public SysResult checkUsername(@RequestParam(value="username")String userName){
		//调用service,处理数据,查询结果返回SysResult对象
		//status=0表示userName不存在,=1表示存在
		SysResult result=userService.checkUsername(userName);
		return result;
	}
	
	//表单提交数据新增
	@RequestMapping("user_ajax/regist")
	@ResponseBody
	public SysResult saveUser(User user){
		try{
			int success=userService.saveUser(user);
			//返回的数据处理新增rows=1
			if(success==1){//新增成功
			return SysResult.build(1, "");
			}
			return SysResult.build(2, "新增用户失败");
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(2, e.getMessage());
		}
	}
	
	//登录
	@RequestMapping("user_ajax/login")
	@ResponseBody
	public SysResult doLogin(User user,HttpServletRequest request,
			HttpServletResponse response){
		//拿到user数据,userName/userPassword;
		String ticket=userService.doLoin(user);
		//判断是否为空
		if(StringUtils.isNotEmpty(ticket)){
			//非空说明登录成功,需要将ticket存储到cookie响应会页面
			//cookie的名称叫做JT_TICKET值就是ticket
			CookieUtils.setCookie(request, response, 
					"JT_TICKET", ticket);
			return SysResult.build(1, "ok");
		}else{//说明ticket为空,说明登录失败
			return SysResult.build(0, "登录失败");
		}
		
	}
	/*
	//登录
	@RequestMapping("user_ajax/login")
	@ResponseBody
	public SysResult doLogin(User user,HttpServletRequest request,HttpSession session){
		//调用查询结果返回	SysResult
		SysResult result=userService.doLogin(user);
		//session携带数据
		HttpSession session1 = request.getSession(true);
		if(result.getStatus()==1){//登录成功,session对象携带数据
			User _user=(User)result.getData();
			session.setAttribute("userName", _user.getUserName());
			session.setAttribute("userId", _user.getUserId());
		}
		return result;
	}
	//注销逻辑
	@RequestMapping("user_ajax/logout")
	public String logout(HttpSession session){
		//会话没有断开情况下,session对象是同一个
		session.removeAttribute("userName");
		session.removeAttribute("userId");
		return "index";
	}*/
}













































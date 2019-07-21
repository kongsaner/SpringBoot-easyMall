package com.jt.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jt.common.config.HttpClientService;
import com.jt.common.pojo.User;
import com.jt.common.util.CookieUtils;
import com.jt.common.util.OUtil;
@Component
public class OrderInterceptor implements HandlerInterceptor{
	@Autowired
	private HttpClientService client;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//获取cookie的JT_TICKET值
		String ticket = CookieUtils.
				getCookieValue(request, "JT_TICKET");
		if(StringUtils.isNotEmpty(ticket)){//至少曾经登陆过
			//发起代码的跨域请求sso.jt.com/user/query/ticket
			String url="http://sso.jt.com"
					+ "/user/query/"+ticket;
			String userJson=client.doGet(url);
			//判断userJson是否为空
			if(StringUtils.isNotEmpty(userJson)){//不为空
				//登录有效
				//将数据userId放到request域中,放行
				User user = OUtil.mapper.
						readValue(userJson, User.class);
				request.setAttribute("userId",
						user.getUserId());
				return true;
			}
		}
		//任何一个if没进去，说明登录失效的
		//跳转登录页面
		response.sendRedirect("/page/login");
		return false;
	
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}

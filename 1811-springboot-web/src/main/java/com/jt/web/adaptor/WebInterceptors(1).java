package com.jt.web.adaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.jt.web.interceptors.CartInterceptor;
import com.jt.web.interceptors.OrderInterceptor;
import com.jt.web.interceptors.SeckillInterceptor;
@Component
public class WebInterceptors extends WebMvcConfigurerAdapter{
	@Autowired
	private CartInterceptor ci;
	@Autowired
	private OrderInterceptor oi;
	@Autowired
	private SeckillInterceptor si;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(ci)
		.addPathPatterns("/cart/**");
		registry.addInterceptor(oi)
		.addPathPatterns("/order/**");
		registry.addInterceptor(si)
		.addPathPatterns("/seckill/**");
		super.addInterceptors(registry);
	}
	

}

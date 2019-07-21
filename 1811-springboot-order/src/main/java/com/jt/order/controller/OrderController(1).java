package com.jt.order.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.common.pojo.Order;
import com.jt.common.util.OUtil;
import com.jt.common.util.UUIDUtil;
import com.jt.common.vo.SysResult;
import com.jt.order.mapper.OrderMapper;

@RestController
@RequestMapping("order")
public class OrderController {
	
	//查询我的订单们
	/*
	请求地址	order.jt.com/order/query/{userId}
	请求参数	路径传参String userId
	请求方式	get
	返回数据	List<Order>*/
	@Autowired
	private OrderMapper orderMapper;
	@RequestMapping("query/{userId}")
	public List<Order> queryList(@PathVariable String userId){
		List<Order> oList=orderMapper.queryOrders(userId);
		return oList;
	}
	//新增订单
	/*
	新增订单
	请求地址	order.jt.com/order/save
	请求参数	一个Order对象的json字符串;
		请求体的所有数据接收;
		将json转化成Order对象,补充缺少的字段
	请求方式	post
	返回数据	成功200,失败201SysResult对象*/
	@RequestMapping("save")
	public SysResult saveOrder(@RequestBody String json){
		//RequestBody,就是将请求的体中所有内容,赋值给注解所在的变量
		//转化对象,单个的order对象
		try{
			Order order = OUtil.mapper.readValue(json, Order.class);
			//orderId,是空的 paystate是空,orderTime
			order.setOrderId(UUIDUtil.getUUID());
			order.setOrderPaystate(0);
			order.setOrderTime(new Date());
			orderMapper.addOrder(order);
			return SysResult.oK();
		}catch(Exception e){
			return SysResult.build(201, e.getMessage());
		}
	}
	//删除订单
	/*
	删除订单
	请求地址	order.jt.com/order/delete/{orderId}
	请求参数	路径传参String orderId
	请求方式	get
	返回数据	200成功201失败SysResult*/
	@RequestMapping("delete/{orderId}")
	public SysResult deleteOrder(@PathVariable String orderId){
		orderMapper.deleteOrder(orderId);
		return SysResult.oK();
	}
}






























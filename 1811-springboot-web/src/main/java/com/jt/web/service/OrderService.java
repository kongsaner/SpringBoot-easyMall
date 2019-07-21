package com.jt.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.config.HttpClientService;
import com.jt.common.pojo.Order;
import com.jt.common.util.OUtil;

@Service
public class OrderService {
	@Autowired
	private HttpClientService client;
	private ObjectMapper mp=OUtil.mapper;
	public List<Order> queryOrders(String userId) {
		String url="http://order.jt.com/order/query/"+userId;
		try{
			String jsonData=client.doGet(url);//list的json字符串
			//jsonNode解析
			JsonNode data = mp.readTree(jsonData);
			//准备返回的内容
			List<Order> oList=null;
			if(data.isArray()&&data.size()>0){
				oList=mp.readValue(data.traverse(), 
						mp.getTypeFactory().
						constructCollectionType(List.class, 
								Order.class));
			}
			return oList;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	public void addOrder(Order order) {
		//httpcleint发送请求,参数不是使用map对象,将order转化成
		//json字符串,交给后台接收,
		try{
			String orderJson=OUtil.mapper.writeValueAsString(order);
			String url="http://order.jt.com/order/save";
			client.doPostJson(url, orderJson);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	public void deleteOrder(String orderId) {
		try{
			String url="http://order.jt.com/order/delete/"+orderId;
			client.doGet(url);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}

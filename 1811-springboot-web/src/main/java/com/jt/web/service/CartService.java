package com.jt.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.config.HttpClientService;
import com.jt.common.pojo.Cart;
import com.jt.common.util.OUtil;

@Service
public class CartService {
	@Autowired
	private HttpClientService client;
	private ObjectMapper mp=OUtil.mapper;
	public List<Cart> queryCart(String userId) {
		//按照接口实现访问
		try{
			String url="http://cart.jt.com/cart/list/"+userId;
			String jsonData=client.doGet(url);//[{"":""},{}]
			//jsonData是一个list的结构,无法直接使用之前readValue();
			//先将jsonData的字符串,转化成JsonNode
			JsonNode data = mp.readTree(jsonData);
			List<Cart> cList=null;
			//data并不一定有值,判断是否是数组类型,是否size>0
			if(data.isArray()&&data.size()>0){
				//(将json解析成linkedHashMap的封装对象),traverce()
				cList=mp.readValue(data.traverse(),
						mp.getTypeFactory()
						.constructCollectionType(List.class, 
								Cart.class));
			}
			//并且指定 List类反射,元素类反射对象 List<Cart>
			return cList;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public void addCart(String userId, String productId, Integer num) throws Exception {
		String url="http://cart.jt.com/cart/save";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("productId", productId);
		map.put("num", num);
		//doGet
		client.doGet(url, map);
		
	}
	public void updateNum(String userId, String productId, Integer num) throws Exception {
		String url="http://cart.jt.com/cart/updateNum";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("productId", productId);
		map.put("num", num);
		client.doGet(url,map);
		
	}
	public void deleteCart(String userId, String productId) throws Exception {
		String url="http://cart.jt.com/cart/delete";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("productId", productId);	
		client.doGet(url,map);
	}

}

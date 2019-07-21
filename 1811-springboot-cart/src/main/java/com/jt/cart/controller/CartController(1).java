package com.jt.cart.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.cart.mapper.CartMapper;
import com.jt.common.config.HttpClientService;
import com.jt.common.config.RedisService;
import com.jt.common.pojo.Cart;
import com.jt.common.pojo.Product;
import com.jt.common.util.OUtil;
import com.jt.common.vo.SysResult;

@RestController
@RequestMapping("cart")
public class CartController {
	/*
	 请求连接	cart.jt.com/cart/list/{userId}
	请求方式	Get
	请求参数	String userId 路径传参
	返回数据	List<Cart>对象的json字符串
		[{“”：“”},{},{},{}]
	备注	web前台获取json结果如何使用mapper解析,
	不能直接调用readValue();
	 */
	@Autowired
	private CartMapper cartMapper;
	@RequestMapping("list/{userId}")
	public List<Cart> queryMyCart(@PathVariable 
			String userId){
		return cartMapper.queryCart(userId);
	}
	
	@Autowired
	private RedisService redis;
	@Autowired
	private HttpClientService client;
	
	/*
	请求地址	cart.jt.com/cart/save
	请求方式	post
	请求参数	Cart cart(只有userId,productId,num) productPrice,productImg productName
	备注	
	从redis获取商品数据,缓存,当缓存没有数据时,httpclient调用manage工程获取queryById的数据	
	返回数据	SysResult对象jsonstatus=200成功，其他表示失败

	 */
	@RequestMapping("save")
	public SysResult saveCart(Cart cart){
		try{
			//先访问缓存,获取商品数据
			String key="product_"+cart.getProductId();
			String jsonData=redis.get(key);
			//准备一个接受商品数据的对象
			Product product =null;
			if(StringUtils.isNotEmpty(jsonData)){//缓存有数据
				product = 
				OUtil.mapper.readValue(jsonData, Product.class);
			}else{//缓存未命中
				//访问manage.jt.com获取数据
				String url="http://manage.jt.com/products/queryById/"
						+cart.getProductId();
				jsonData=client.doGet(url);
				product = 
				OUtil.mapper.readValue(jsonData, Product.class);	
			}
			//将easymall中新增购物车的service粘贴
			//判断是否已经存在
			Cart exist=cartMapper.queryOne(cart);
			//System.out.println(exist);
			//如果存在,更新num
			if(exist!=null){//将已存在num和新增num添加,执行
				//更新语句
				exist.setNum(exist.getNum()+cart.getNum());
				cartMapper.updateNum(exist);//userId,productId,num
			}else{
				cart.setProductImage(product.getProductImgurl());
				cart.setProductPrice(product.getProductPrice());
				cart.setProductName(product.getProductName());
				cartMapper.addCart(cart);
			}
			return SysResult.oK();
		}catch(Exception e){
			return SysResult.build(201, e.getMessage());
		}
	}
	//更新商品数量购物车
	/*
	请求地址	http://cart.jt.com/cart/updateNum
	请求参数	String userId,String productId,Integer num
	请求方式	get
	返回数据	SysResult对象jsonstatus=200成功，其他表示失败
	 */
	@RequestMapping("updateNum")
	public SysResult updateNum(Cart cart){
		//直接执行更新
		try{
			cartMapper.updateNum(cart);
			return SysResult.oK();
		}catch(Exception e){
			return SysResult.build(201, e.getMessage());
		}
	}
	
	//删除购物车数据
	/*
	请求地址	cart.jt.com/cart/delete
	请求参数	String userId,String productId
	请求方式	get
	返回数据	SysResult对象jsonstatus=200成功，其他表示失败
	 */
	@RequestMapping("delete")
	public SysResult deleteCart(Cart cart){
		cartMapper.deleteCart(cart);
		return SysResult.oK();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.common.config.HttpClientService;
import com.jt.common.config.RedisService;
import com.jt.common.pojo.Product;
import com.jt.common.util.OUtil;
import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.HttpResult;
import com.jt.common.vo.Page;

@Service
public class ProductService {
	//商品分页查询,利用HttpClient发起请求,到后台工程
	//从后台工程后去当前分页,数据的page对象解析
	@Autowired
	private HttpClientService client;
	public Page queryByPage(Integer currentPage, Integer rows) {
		/*url访问地址,携带参数
		 *get请求发送
		 *接收响应内容json
		 *解析返回
		 */
		try{
			String url="http://manage.jt.com"
					+ "/products/pageQuery?page="+currentPage+
					"&rows="+rows;
			String jsonData = client.doGet(url);
			//解析page对象的json为page对象
			Page page=OUtil.mapper.readValue(jsonData, Page.class);
			return page;
		}catch(Exception e){
			System.out.println("连接后台出现异常:"+e.getMessage());
			return null;
		}
	}
	@Autowired(required=false)
	private RedisService redis;//application
	//文件需要配置;
	public Product queryById(String productId) {
		/*生成key值:key与业务有关于唯一值有关
		 *判断缓存存在
		 *	存在则直接使用,
		 *  不存在则访问后台*/
		//查询单个商品的缓存,配合httpClient访问后台
		try{
			String key="product_"+productId;
			if(redis.exists(key)){//有则直接使用
				String jsonData=redis.get(key);
				Product product = OUtil.mapper.readValue(jsonData, Product.class);
				return product;
			}else{//不存在则访问后台manage系统获取返回json
				String url="http://manage.jt.com"
						+ "/products/queryById/"+productId;
				//没有别的参数,doGet(url)
				String jsonData=client.doGet(url);
				//set缓存存储
				redis.set(key, jsonData);
				//解析数据
				Product product = OUtil.mapper.readValue(jsonData, Product.class);
				return product;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	
		
	}
	public EasyUIResult manageQueryList(Integer page, Integer rows) {
		//url,发起get请求,接收,解析
		try{
			String url="http://manage.jt.com"
					+ "/products/queryPages?page="+page
					+"&rows="+rows;
			String jsonData = client.doGet(url);//EasyUIResut的json
			EasyUIResult result = 
					OUtil.mapper.
					readValue(jsonData, EasyUIResult.class);
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public int saveProduct(Product product) {
		String url="http://manage.jt.com/products/save";
		//map保存多个key-value,每一个key-value都是product属性名和属性值
		Map<String,Object> map=new HashMap<String,Object>();
		//封装所有参数
		map.put("productName", product.getProductName());
		map.put("productPrice", product.getProductPrice());
		map.put("productImgurl", product.getProductImgurl());
		map.put("productCategory", product.getProductCategory());
		map.put("productNum", product.getProductNum());
		map.put("productDescription", product.getProductDescription());
		try{
			//doget区别返回结果,封装的是HttpResult
			//获取响应体,getBody()方法
			HttpResult result =client.doPost(url, map);
			int success=Integer.parseInt(result.getBody());//"1","0"
			return success;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
		
		
	}
	public int updateProduct(Product product) {
		//将url地址,换成接口的更新地址
		String url="http://manage.jt.com/products/update";
		//map保存多个key-value,每一个key-value都是product属性名和属性值
		Map<String,Object> map=new HashMap<String,Object>();
		//封装所有参数
		map.put("productName", product.getProductName());
		map.put("productPrice", product.getProductPrice());
		map.put("productImgurl", product.getProductImgurl());
		map.put("productCategory", product.getProductCategory());
		map.put("productNum", product.getProductNum());
		map.put("productDescription", product.getProductDescription());
		map.put("productId", product.getProductId());
		try{
			//doget区别返回结果,封装的是HttpResult
			//获取响应体,getBody()方法
			HttpResult result =client.doPost(url, map);
			int success=Integer.parseInt(result.getBody());//"1","0"
			return success;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	
	/*@Autowired(required=false)
	 * 查询单个商品,利用productId,转发给mapper.调用
	 * queryById的抽象方法,绑定同名的<select>
	 
	public Product queryById(String productId) {
		//生成key;
		String key="product_"+productId;
		try{
			ObjectMapper mapper=new ObjectMapper();
			//判断缓存是否命中
			if(jedis.exists(key)){
				//解析获取的数据
				String jsonData=jedis.get(key);
				Product product=
				mapper.readValue(jsonData, Product.class);
				//直接将数据返回调用
				return product;
			}else{
				//缓存没有
				Product product=productMapper.queryById(productId);
				//添加缓存,value类型? String,将product转化成json字符串
				//jedis.hsetnx(key, field, value)
				
				//转化成json
				String jsonData=mapper.writeValueAsString(product);
				jedis.set(key, jsonData);
				return product;
			}
			Product product=productMapper.queryById(productId);
			return product;
		}catch(Exception e){
			//异常出现,可以与redis有关,数据无法从redis获取,依然到数据库执行
			Product product=productMapper.queryById(productId);
			return product;
		}
		
		Product product=productMapper.queryById(productId);
		return product;
	}
	@Autowired(required=false)
	private RedisService redis;
	@Autowired(required=false)
	private RabbitTemplate temp;
	
	public int saveProduct(Product product) {
		//将product缺少的数据补充 productId
		//uuid的代码实现获取字符串唯一值
		product.setProductId(UUIDUtil.getUUID());
		//添加缓存逻辑
		ObjectMapper mapper=new ObjectMapper();
		String jsonData = mapper.writeValueAsString(product);
		String key="product_"+product.getProductId();
		redis.set(key, jsonData);
		//将消息发送到rabbitmq,实现异步处理缓存入库
		//发送jsonData,也可以发送productId
		try{
			temp.convertAndSend("easymallDEX", "product.add", product.getProductId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return productMapper.saveProduct(product);
	}
	public EasyUIResult manageQueryList(Integer page, Integer rows) {
		int total=productMapper.queryCount();
		List<Product> pList = productMapper.queryByPage((page-1)*rows, rows);
		//封装EasyUIResult
		EasyUIResult result=new EasyUIResult();
		result.setRows(pList);
		result.setTotal(total);
		return result;
	}
	public int updateProduct(Product product) {
		
		return productMapper.updateProduct(product);
	}
	*/
}

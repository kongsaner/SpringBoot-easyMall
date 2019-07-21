package com.jt.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.pojo.Product;
import com.jt.common.util.UUIDUtil;
import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.Page;
import com.jt.manage.mapper.ProductMapper;

@Controller
public class ProductController {
	
	/*请求地址	http://manage.jt.com/products/pageQuery?page=1&rows=5
		请求方式	get请求
		请求参数	Integer page Integer rows
		返回数据	后台返回给前台使用的Page对象的json字符串;
		备注	前台将json解析,根据域前端的接口与页面数据交互使用 */
	@Autowired
	private ProductMapper productMapper;
	@RequestMapping("products/pageQuery")
	@ResponseBody
	public Page queryByPage(Integer page,Integer rows){
		//封装page对象,需要当前页,需要plist查询结果,需要totalPage
		Integer start=(page-1)*rows;
		//注入mapper
		//利用起始位置和rows查询分页数据
		List<Product> pList=productMapper
				.queryByPage(start,rows);
		Page pageD=new Page();
		pageD.setCurrentPage(page);
		pageD.setProducts(pList);
		//totalPage,商品总页数,根据rows不同改变
		int total=productMapper.queryCount();
		//total,rows,计算totalPage;
		Integer totalPage=(total%rows==0?total/rows:(total/rows+1));
		pageD.setTotalPage(totalPage);
		return pageD;
	}
	//商品的id查询
	@RequestMapping("products/queryById/{productId}")
	@ResponseBody
	public Product queryById(@PathVariable String productId){
		Product product = productMapper.queryById(productId);
		//缓存??实现前台缓存即可
		return product;
	}
	//给商家页面查询的分页
	@RequestMapping("products/queryPages")
	@ResponseBody
	public EasyUIResult queryPages(Integer page,Integer rows){
		int total=productMapper.queryCount();
		List<Product> pList = productMapper.
				queryByPage((page-1)*rows, rows);
		//封装EasyUIResult
		EasyUIResult result=new EasyUIResult();
		result.setRows(pList);
		result.setTotal(total);
		return result;
	}
	//商品的新增逻辑
	@RequestMapping("products/save")
	@ResponseBody
	public String saveProduct(Product product){
		//product数据不全
		product.setProductId(UUIDUtil.getUUID());
		//mybatis,做了一件事;如果id,是int 自增,新增完毕的时候,
		//product的id也是有数据的,每个数据库的客户端线程,在新增完毕后,mybatis持久层
		//自动调用一个select last_insert_id(),操作是线程安全的
		try{
			productMapper.saveProduct(product);
			return "1";
		}catch(Exception e){
			return "0";
		}
	}
	
	//商品的更新
	@RequestMapping("products/update")
	@ResponseBody
	public String updateProduct(Product product){
		productMapper.updateProduct(product);
		return "1";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

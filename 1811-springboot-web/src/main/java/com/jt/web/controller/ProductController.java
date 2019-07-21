package com.jt.web.controller;

import java.util.List;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.pojo.Product;
import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.Page;
import com.jt.common.vo.SysResult;
import com.jt.web.service.ProductService;


@Controller
public class ProductController {
	@Autowired
	private ProductService productService;
	//全部商品的分页查询
	@RequestMapping("/product/page")
	public String queryByPage(Integer currentPage,Integer rows,Model model){
		
		Page page=productService.queryByPage(currentPage,rows);
		model.addAttribute("page", page);
		return "product_list";
	}
	
	//单个商品根据id查询数据,返回商品对象
	@RequestMapping("product/findProductById/{productId}")
	public String queryById(@PathVariable String productId,
			Model model){
		
		Product product=productService.queryById(productId);
		model.addAttribute("product", product);
		return "product_info";
	}
	//后台分页查询
	@RequestMapping("product/query")
	@ResponseBody
	public EasyUIResult manageQueryList(@RequestParam(defaultValue="1")Integer page,
			Integer rows){
		
		EasyUIResult result=productService.manageQueryList(page,rows);
		return result;
	}
	
	@RequestMapping("product/save")
	@ResponseBody
	public SysResult saveProduct(Product product){
		//判断存储成功失败,成功返回200的sysresult,失败返回201
		//利用执行结果 int success=1/0,异常判断成功失败
		try{
			int success=productService.saveProduct(product);
			//success=1表示数据库新增成功
			if(success==1){
				return SysResult.oK();
				//ok的静态方法,返回一个status=200,msg=ok的对象
			}
			return SysResult.build(201, "新增失败");
			//类似于ok的静态方法,build返回一个传递参数封装的SysResult对象
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(201, e.getMessage());
		}
	}
	@RequestMapping("product/update")
	@ResponseBody
	public SysResult updateProduct(Product product){
		//利用返回值,和异常判断成功失败
		try{
			//业务层处理业务逻辑,返回更新成功失败的1/0
			int success=productService.updateProduct(product);
			if(success==1){//成功
				return SysResult.oK();
			}
			return SysResult.build(201, "更新失败");
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(201, e.getMessage());
		}
	}
	/*
	//新增商品数据到数据库,insert
	//参数本质:key1=value1&key2=value2
	//不需要转向页面,讲返回值,全部放到响应体提供给ajax调用
	
	
	//商品的修改
	
	
	//添加一个访问方法,将product数据存储在es中
	@Autowired(required=false)
	private TransportClient client;
	
	@RequestMapping("creating")
	@ResponseBody
	public String createData() throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		Page page = productService.queryByPage(1, 100);
		List<Product> pList =(List<Product>)page.getProducts();
		//数据的新增,每新增一个document,都对应一个product对象
		for (Product product : pList) {
			String source=mapper.writeValueAsString(product);
			client.prepareIndex("emdb", "product",
					product.getProductId()).setSource(source).get();
		}
		return "success";
	}*/
}








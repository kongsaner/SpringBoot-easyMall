package com.jt.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.common.pojo.Cart;
import com.jt.web.service.CartService;



@Controller
public class CartController {
	
	//查询我的购物车
	//通过userId获取t_cart表格的list数据
	@Autowired
	private CartService cartService;
	@RequestMapping("cart/mycart")
	public String queryCart(Model model,HttpServletRequest request){
		//判断登录是否存在,session是否有userId属性
		String userId=(String) request.getAttribute("userId");
		
		//已经登录情况下,拿到了userId,直接调用方法获取cart购物车数据
		List<Cart> cartList=cartService.queryCart(userId);
		//model携带数据到页面 "cartList"
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	@RequestMapping("cart/addCart/{productId}/{num}")
	public String addCart(@PathVariable String productId,
			@PathVariable Integer num,HttpServletRequest request) throws Exception{
		//判断登录是否存在,session是否有userId属性
		String userId=(String) request.getAttribute("userId");
		//主功能编写
		cartService.addCart(userId,productId,num);
		return "redirect:/cart/mycart";
	}
	//更新商品数量
	@RequestMapping("cart/editCart/{productId}/{num}")
	public String editCart(@PathVariable String productId,
			@PathVariable Integer num,
			HttpServletRequest request) throws Exception{
		//获取userId
		String userId=(String)request.getAttribute("userId");
		cartService.updateNum(userId,productId,num);
		return "redirect:/cart/mycart";
	}
	//删除购物车商品
	@RequestMapping("cart/deleteCart/{productId}")
	public String deleteCart(@PathVariable String productId,
			HttpServletRequest request) throws Exception{
		//获取userId
		String userId=(String)request.getAttribute("userId");
		cartService.deleteCart(userId,productId);
		return "redirect:/cart/mycart";
	}
	
}





















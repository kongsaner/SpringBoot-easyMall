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
import com.jt.common.pojo.Order;
import com.jt.web.service.CartService;
import com.jt.web.service.OrderService;


@Controller
public class OrderController {
	@Autowired
	private OrderService orderService;
	//查询我的订单
	@RequestMapping("order/list")
	public String queryOrders(HttpServletRequest request,
			Model model){
		String userId=(String) request.getAttribute("userId");
		List<Order> orderList=orderService.queryOrders(userId);
		//携带数据
		model.addAttribute("orderList", orderList);
		return "order_list";
	}
	@Autowired
	private CartService cartService;
	//新增订单第一步
	@RequestMapping("order/order-cart")
	public String goOrderSubmit(HttpServletRequest request,
			Model model){
		//调用cartService,获取cartList
		String userId=(String)request.getAttribute("userId");
		//cartService
		List<Cart> cartList=cartService.queryCart(userId);
		//携带到页面展示
		model.addAttribute("cartList",cartList);
		return "order_add";
	}
	//新增订单第二步
	@RequestMapping("order/addOrder")
	public String addOrder(Order order,HttpServletRequest request){
		//获取userId
		String userId=(String)request.getAttribute("userId");
		//orderService
		order.setUserId(userId);
		orderService.addOrder(order);
		//重定向到查询我的order页面
		return "redirect:/order/list";
	}

	
	//删除订单
	
	@RequestMapping("order/deleteOrder/{orderId}")
	public String deleteOrder(@PathVariable String orderId){
		orderService.deleteOrder(orderId);
		return "redirect:/order/list";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

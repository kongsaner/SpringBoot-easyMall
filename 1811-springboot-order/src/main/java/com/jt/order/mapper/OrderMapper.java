package com.jt.order.mapper;

import java.util.Date;
import java.util.List;

import com.jt.common.pojo.Order;

public interface OrderMapper {

	List<Order> queryOrders(String userId);

	void addOrder(Order order);

	void deleteOrder(String orderId);
	
	void deleteOTOrder(Date otTime);
}

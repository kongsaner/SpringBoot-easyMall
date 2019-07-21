package com.jt.cart.mapper;

import java.util.List;

import com.jt.common.pojo.Cart;

public interface CartMapper {

	List<Cart> queryCart(String userId);

	Cart queryOne(Cart _cart);

	void updateNum(Cart exist);

	void addCart(Cart _cart);

	void deleteCart(Cart cart);

}

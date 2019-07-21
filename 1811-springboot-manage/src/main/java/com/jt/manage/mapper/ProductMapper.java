package com.jt.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jt.common.pojo.Product;

public interface ProductMapper {
	//Param定义sql语句,select标签中,使用的参数,编译传值
	//多个数据的使用,必须定义参数名称才能#{start}传递数值
	List<Product> queryByPage(@Param(value="start")Integer start, @Param(value="rows")Integer rows);

	int queryCount();
	
	Product queryById(String productId);

	int saveProduct(Product product);

	int updateProduct(Product product);

}

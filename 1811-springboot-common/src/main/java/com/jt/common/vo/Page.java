package com.jt.common.vo;

import java.util.List;

public class Page {
	private Integer totalPage;
	private Integer currentPage;
	private List<?> products;
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public List<?> getProducts() {
		return products;
	}
	public void setProducts(List<?> products) {
		this.products = products;
	}
	
}

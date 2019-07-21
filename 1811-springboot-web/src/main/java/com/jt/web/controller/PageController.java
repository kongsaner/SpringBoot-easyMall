package com.jt.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	

	@RequestMapping("/")
	public String goIndex(){
		return "index";
	}
	
	@RequestMapping("page/{pageName}")
	public String goPage(@PathVariable String pageName){
		return pageName;
	}
}

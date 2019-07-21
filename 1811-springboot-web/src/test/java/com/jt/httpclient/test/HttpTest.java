package com.jt.httpclient.test;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

/**
 * 使用代码发起http请求
 * 访问京东,访问easymall工程的某个功能
 * @author tedu
 *
 */
public class HttpTest {
	//访问京东
	@Test
	public void jdTest() throws Exception{
		//请求对象,请求url地址,发起请求的httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		//请求地址
		String url="http://www.jd.com";
		//请求request,根据请求方式,构造不同的对象,get,post
		HttpGet request=new HttpGet(url);
		CloseableHttpResponse response = client.execute(request);//执行请求,接收响应
		//数据,页面信息,头数据,都封装到了response对象中,从中解析响应体内容
		HttpEntity entity = response.getEntity();//entity对象封装了响应体
		//从entity打印字符串信息的响应体,html格式的文本数据
		System.out.println(EntityUtils.toString(entity));
	}
	//访问京东
	@Test
	public void easymallTest() throws Exception{
		//请求对象,请求url地址,发起请求的httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		//请求地址
		String url="http://localhost:8091/product/query?page=1&rows=5";
		//请求request,根据请求方式,构造不同的对象,get,post
		HttpGet request=new HttpGet(url);
		CloseableHttpResponse response = client.execute(request);//执行请求,接收响应
		//数据,页面信息,头数据,都封装到了response对象中,从中解析响应体内容
		HttpEntity entity = response.getEntity();//entity对象封装了响应体
		//从entity打印字符串信息的响应体,html格式的文本数据
		System.out.println("获取easymall响应结果:"+EntityUtils.toString(entity));
	}
}

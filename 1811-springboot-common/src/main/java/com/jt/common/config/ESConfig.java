package com.jt.common.config;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="cluster")
//在properties配置文件中
//相同的内容非常多,对当前的属性读取值,使用前缀赋值
//cluster前缀,扫描到前缀配置注解时,会利用类中的getter.setter方法
//将数据拼接到属性中nodes调用setter方法 cluster.nodes
//name的setter方法,cluster.name
public class ESConfig {
	
	private String nodes;
	private String name;
	@Bean
	public TransportClient initialize(){
	try{
		//准备一个setting配置对象,集群名称可以定义elasticsearch
		Settings setting=Settings.builder().
				put("cluster.name", name).build();
		//创建连接对象
		TransportClient client=new PreBuiltTransportClient(setting);
		//解析nodes数据
		String[] hostAndPort = nodes.split(",");
		for (String node : hostAndPort) {
			//10.9.9.9:9300
			String host=node.split(":")[0];
			int port=Integer.parseInt(node.split(":")[1]);
			//指定ip,端口,按照需要的连接,add节点信息,准备3个节点的对象
			InetSocketTransportAddress ISTA1=
					new InetSocketTransportAddress(
							InetAddress.getByName(host), port);
			client.addTransportAddress(ISTA1);
		}
			return client;
			
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	public String getNodes() {
		return nodes;
	}
	public void setNodes(String nodes) {
		this.nodes = nodes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

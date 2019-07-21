package com.jt.common.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

/**//**
 * 利用配置类,完成pool'对象的初始化过程
 * 并且交给spring框架维护对象
 * @author tedu
 *
 *//*
*/@Configuration
public class JedisShardConfig {
	@Value("${spring.redis.nodes:null}")
	private String nodes;
	@Value("${spring.redis.config.maxTotal:0}")
	private Integer maxTotal;
	@Value("${spring.redis.config.maxIdle:0}")
	private Integer maxIdle;
	@Value("${spring.redis.config.minIdle:0}")
	private Integer minIdle;
	
	//创建连接池对象,初始化连接池对象
	@Bean //作用,方法注解,将方法的返回值,交给spring框架维护,相当于bean标签
	public ShardedJedisPool init(){
		try{
			//收集节点信息
			List<JedisShardInfo> infoList=new ArrayList<JedisShardInfo>();
			//将nodes字符串截取 ","{"10.9.39.13:6379","",""}
			String[] hostAndPort = nodes.split(",");//"{"10.9.39.13:6379","",""}
			for (String node : hostAndPort) {
				//node="10.9.39.13:6379" 
				String host=node.split(":")[0];
				int port=Integer.parseInt(node.split(":")[1]);
				infoList.add(new JedisShardInfo(host,port));
			}
			//配置连接池的属性 
			GenericObjectPoolConfig config=new GenericObjectPoolConfig();
			config.setMaxTotal(maxTotal);
			config.setMaxIdle(maxIdle);
			config.setMinIdle(minIdle);
			//分片连接池
			ShardedJedisPool pool=new ShardedJedisPool(config, infoList);
			return pool;
		}catch(Exception e){
			//依赖了工具工程的系统没有配置相关属性
			//初始化方法报错
			e.printStackTrace();
			return null;
		}
		
	}
}

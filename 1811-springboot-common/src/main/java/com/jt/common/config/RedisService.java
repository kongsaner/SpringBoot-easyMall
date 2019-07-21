package com.jt.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {
	//必须添加required=false,如果对象为空,不注入使用
	@Autowired(required=false)
	private ShardedJedisPool pool;
		
	//set,get,expire,exists,ttl,等等
	public String get(String key){
		ShardedJedis jedis = pool.getResource();
		String value=jedis.get(key);
		pool.returnResource(jedis);
		return value;
	}
	public void set(String key,String value){
		ShardedJedis jedis = pool.getResource();
		jedis.set(key,value);
		pool.returnResource(jedis);
		
	}
	public Boolean exists(String key){
		ShardedJedis jedis = pool.getResource();
		Boolean exists=jedis.exists(key);
		pool.returnResource(jedis);
		return exists;
	}
}

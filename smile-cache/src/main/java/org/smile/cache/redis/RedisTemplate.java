package org.smile.cache.redis;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.smile.io.serialize.JdkSerializer;
import org.smile.io.serialize.Serializer;
import org.smile.io.serialize.StringSerializer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Pool;
/**
 * 定义一个操作redis的模板方式
 * 统一进行连接的获取与释放
 * @author 胡真山
 *
 */
public class RedisTemplate {
	/**
	 * redis服务器的址
	 */
	protected String host = "127.0.0.1";
	/**
	 * 连接端口
	 */
	protected int port = 6379;
	/**
	 * 连接数据库
	 */
	protected int database = 1;
	/**
	 * 连接密码
	 */
	protected String password = null;
	/**
	 * 连接超时时长
	 */
	protected int timeout = Protocol.DEFAULT_TIMEOUT;
	/**集群时使用,哨兵模式 主节点*/
	protected String sentinelMaster = null;
	/**哨兵列表*/
	protected Set<String> sentinelSet = null;
	/**redis连接对象池 使用apache pool2 实现*/
	protected Pool<Jedis> redisPool;
	/**redis配置信息*/
	protected JedisPoolConfig redisPoolConfig = new JedisPoolConfig();
	
	/** 对象序列化 */
	protected Serializer<Object> serializer=new JdkSerializer();
	/**
	 * 用于对缓存键的序列化
	 */
	protected Serializer<String> keySerializer=new StringSerializer();

	/**实始化连接池内容*/
	public void init() {
		String pwd = this.password;
		if ("".equals(pwd)) {
			pwd = null;
		}
		if (sentinelSet != null) {//哨兵群集时
			if (sentinelSet != null && sentinelSet.size() > 0) {
				redisPool = new JedisSentinelPool(sentinelMaster, sentinelSet, this.redisPoolConfig, timeout, pwd);
			} else {
				throw new RuntimeException("Error configuring Redis Sentinel connection pool");
			}
		}else{
			redisPool = new JedisPool(this.redisPoolConfig, host, port, timeout, pwd);
		}
	}
	
	/**获到redis连接信息*/
	protected Jedis getJedis(){
		return redisPool.getResource();
	}
	
	/**
	 * 执行一个redis数据操作
	 * @param callback
	 * @return
	 */
	public <T> T execute(RedisCallback<T> callback)
	{
	    Jedis jedis = null;
	    try
	    {	jedis=getJedis();
	    	jedis.select(database);
	        return callback.onCallRedis(jedis);
	    }catch (JedisConnectionException e){
	        if (jedis != null){
	            redisPool.returnBrokenResource(jedis);
	            jedis = null;
	        }
	        throw e;
	    }finally{
	        if (jedis != null){
	            redisPool.returnResource(jedis);
	        }
	    }
	}
	
	public <T> T execute(RedisCallback<T> callback,boolean bool){
		return execute(callback);
	}


	public void setHost(String host) {
		this.host = host;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public void setDatabase(int database) {
		this.database = database;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}


	public void setSentinelMaster(String sentinelMaster) {
		this.sentinelMaster = sentinelMaster;
	}
	
	public void setSentinelSets(String sentinelSets){
		this.sentinelSet=new LinkedHashSet<String>(Arrays.asList(sentinelSets.split(";")));
	}
	
	public void destroy(){
		redisPool.destroy();
	}

	public Serializer<Object> getSerializer() {
		return serializer;
	}

	public Serializer<String> getKeySerializer() {
		return keySerializer;
	}

	public Serializer<String> getStringSerializer() {
		return this.keySerializer;
	}

	public Serializer<Object> getValueSerializer() {
		return this.serializer;
	}

	public void setSentinelSet(Set<String> sentinelSet) {
		this.sentinelSet = sentinelSet;
	}

	public void setRedisPoolConfig(JedisPoolConfig redisPoolConfig) {
		this.redisPoolConfig = redisPoolConfig;
	}

	public void setSerializer(Serializer<Object> serializer) {
		this.serializer = serializer;
	}

	public void setKeySerializer(Serializer<String> keySerializer) {
		this.keySerializer = keySerializer;
	}
	
	
}

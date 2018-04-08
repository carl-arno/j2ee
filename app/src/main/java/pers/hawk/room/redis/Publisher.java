package pers.hawk.room.redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 生产者
 */
public class Publisher implements Runnable {

	private JedisPool jedisPool;

	public Publisher() {
		
		URI uri = null;
		try {
			uri = new URI("http://192.168.199.139:6379/");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		jedisPool = new JedisPool(new JedisPoolConfig(),uri);
	}

	public void start() {
		Jedis jedis = jedisPool.getResource();
		jedis.auth("redis");
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		
		while (true) {
			try {
				System.out.println("please publish messag:");
				line = bufferedReader.readLine();
				if (!"quit".equals(line)) {
					jedis.publish("MsgCMD", line);
				} else {
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		start();
	}

}

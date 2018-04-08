package pers.hawk.room.redis;

import java.net.URI;
import java.net.URISyntaxException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

/**
 * 消费者
 */
public class Subscriber implements Runnable {

	private JedisPool jedisPool;

	private final String channel = "MsgCMD";

	public Subscriber() {
		URI uri = null;
		try {
			uri = new URI("http://192.168.199.139:6379/");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		jedisPool = new JedisPool(new JedisPoolConfig(),uri);
	}

	private JedisPubSub jedisPubSub = new JedisPubSub() {

		public void onMessage(String channel, String message) {
			System.out.println(String.format("onMessage, channel %s, message %s", channel, message));
		}

		@Override
		public void onPMessage(String pattern, String channel, String message) {
			System.out.println(String.format("onPMessage, channel %s, message %s", channel, message));
		}

		public void onSubscribe(String channel, int subscribedChannels) {
			System.out.println(String.format("onSubscribe , channel %s, subscribedChannels %d", channel, subscribedChannels));
		}

		public void onUnsubscribe(String channel, int subscribedChannels) {
			System.out.println(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d", channel, subscribedChannels));
		}

		@Override
		public void subscribe(String... channels) {
			System.out.println(String.format("subscribe, channel %s, ", channel));
		}

		@Override
		public void onPUnsubscribe(String pattern, int subscribedChannels) {
			System.out.println(String.format("onPUnsubscribe, channel %s, ", channel));
		}

		@Override
		public void onPSubscribe(String pattern, int subscribedChannels) {
			System.out.println(String.format("onPSubscribe, channel %s, ", channel));
		}

	};

	Jedis jedis = null;

	public void start() {

		try {

			jedis = jedisPool.getResource();
			jedis.auth("redis");
			
			jedis.subscribe(jedisPubSub, channel);

		} catch (Exception e) {
			System.out.println(String.format("subsrcibe channel error, %s", e));
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public void run() {
		start();
	}

}

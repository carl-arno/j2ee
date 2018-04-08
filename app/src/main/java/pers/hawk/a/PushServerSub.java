package pers.hawk.a;

import java.net.URI;
import java.util.Calendar;

import pers.hawk.room.Injection;
import pers.hawk.room.server.PushServer;
import pers.hawk.room.util.AppConst;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

/**
 * 发送数据服务
 */
public class PushServerSub extends PushServer implements Runnable {

	private JedisPool jedisPool;

	public PushServerSub() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		initMsg();

	}

	public void initMsg() {

		String channel = "MsgCMD";

		try {
			jedisPool = new JedisPool(new JedisPoolConfig(), new URI("http://192.168.199.139:6379/"));
			Jedis jedis = jedisPool.getResource();
			jedis.auth("redis");
			synchronized (mapTabMenu) {
				StringBuffer stringBuffer = null;
				stringBuffer = mapTabMenu.get(Injection.menuString[2]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 控制服务已启动.").append(AppConst.line);
				mapTabMenu.notifyAll();
			}
			jedis.subscribe(jedisPubSub, channel);
		} catch (Exception e) {
			logger.warn(e.toString());
			synchronized (mapTabMenu) {
				StringBuffer stringBuffer = null;
				stringBuffer = mapTabMenu.get(Injection.menuString[3]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 控制服务未能启动,请检查服务器是否运行,10秒后尝试重新连接.").append(AppConst.line);
				mapTabMenu.notifyAll();
			}
			synchronized (format) {
				try {
					format.wait(10 * 1000);
				} catch (InterruptedException e1) {
					logger.warn(e.toString());
					e1.printStackTrace();
				}
				format.notifyAll();
			}
			initMsg();
		}
	}

	/**
	 * 
	 */
	private JedisPubSub jedisPubSub = new JedisPubSub() {
		public void onMessage(String channel, String message) {
			StringBuffer stringBuffer = null;
			synchronized (mapTabMenu) {
				stringBuffer = mapTabMenu.get(Injection.menuString[2]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 接收到消息:").append(message).append(AppConst.line);
				mapTabMenu.notifyAll();
			}
		}

		public void onPMessage(String pattern, String channel, String message) {
			onMessage(channel, message);
		}
	};

}

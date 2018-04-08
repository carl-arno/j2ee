package pers.hawk.room;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.h2.jdbcx.JdbcConnectionPool;

import pers.hawk.a.PushServerSub;
import pers.hawk.room.dbserver.DeviceService;
import pers.hawk.room.dbserver.EnergyService;
import pers.hawk.room.dbserver.MsgService;
import pers.hawk.room.local.HMsgService;
import pers.hawk.room.local.HService;
import pers.hawk.room.server.ReceiveServer;
import pers.hawk.room.server.ReceiveServiceImp;
import pers.hawk.room.server.PushServer;
import pers.hawk.room.server.PushServiceImp;
import pers.hawk.room.z.util.ClientSocket;
import pers.hawk.view.frame.Restart;
import pers.hawk.view.frame.TabMenu;

public class Injection {

	public static String[] menuString = new String[] { "事件", "能耗", "控制", "错误" };

	/**
	 * 
	 */
	private Thread thread;

	/**
	 * 本地数据连接池
	 */
	private JdbcConnectionPool jdbcConnectionPool;
	/**
	 * 本地数据（业务层）
	 */
	private HService hService;

	/**
	 * 主数据库连接
	 */
//	private Connection connection;

	public Map<String, TabMenu> mapTabMenu;

	private Map<String, ClientSocket> mapSocket;

	/**
	 * 
	 */
	private ReceiveServer receiveServer;

	/**
	 * 
	 */
	private PushServer pushServer;

	public Injection() {
		super();

		thread = new Thread(new Restart());

		jdbcConnectionPool = JdbcConnectionPool.create("jdbc:h2:./config/config", "sa", "");
		jdbcConnectionPool.setLoginTimeout(10000);
		jdbcConnectionPool.setMaxConnections(10);

		hService = new HService(jdbcConnectionPool);

		// del
		mapTabMenu = new HashMap<String, TabMenu>();
		for (int a = 0; a < menuString.length; a++) {
			TabMenu tabMenu = new TabMenu(a, menuString[a], menuString[a], Color.BLUE, new StringBuffer());
			if (a == menuString.length - 1) {
				tabMenu.setColor(Color.RED);
			}
			mapTabMenu.put(menuString[a], tabMenu);
		}
		// del

		mapSocket = new HashMap<String, ClientSocket>();

	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public JdbcConnectionPool getJdbcConnectionPool() {
		return jdbcConnectionPool;
	}

//	public Connection getConnection() {
//		return connection;
//	}

	public ReceiveServer getReceiveServer() {
		if (null == receiveServer) {
			receiveServer = new ReceiveServer();
			receiveServer.mapTabMenu = mapTabMenu;
			receiveServer.setReceiveService(new ReceiveServiceImp());
			receiveServer.getReceiveService().mapTabMenu = mapTabMenu;
			receiveServer.getReceiveService().mapSocket = mapSocket;
			receiveServer.getReceiveService().setDeviceService(new DeviceService());
			receiveServer.getReceiveService().getDeviceService().setLocalService(hService);
//			receiveServer.getReceiveService().getDeviceService().setConnection(getConnection());
			receiveServer.getReceiveService().setLocalService(hService);
			receiveServer.getReceiveService().setEnergyService(new EnergyService());
			receiveServer.getReceiveService().getEnergyService().setLocalService(hService);
		}
		return receiveServer;
	}

	/**
	 * push 服务
	 */
	public PushServer getPushServer() {
		if (null == pushServer) {
			pushServer = new PushServer();
			pushServer.mapTabMenu = mapTabMenu;
			pushServer.mapSocket = mapSocket;
			pushServer.setLocalMsgService(new HMsgService());
			pushServer.getLocalMsgService().setJdbcConnectionPool(jdbcConnectionPool);
			pushServer.setDeviceService(new DeviceService());
			pushServer.setMsgService(new MsgService());
			pushServer.setPushServiceImp(new PushServiceImp());
			pushServer.getDeviceService().setLocalService(hService);
			pushServer.getMsgService().setLocalService(hService);
			pushServer.getPushServiceImp().setLocalService(hService);
		}
		return pushServer;
	}

	/**
	 * push 服务
	 */
	public PushServer getSendServerSub() {
		PushServerSub sendServerSub = new PushServerSub();
		sendServerSub.mapTabMenu = mapTabMenu;
		sendServerSub.setPushServiceImp(new PushServiceImp());
		sendServerSub.getPushServiceImp().setLocalService(hService);
		return sendServerSub;
	}

}

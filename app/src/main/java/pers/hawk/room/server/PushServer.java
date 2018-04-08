package pers.hawk.room.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import pers.hawk.room.Injection;
import pers.hawk.room.dbserver.DeviceService;
import pers.hawk.room.dbserver.BeanMsg;
import pers.hawk.room.dbserver.MsgService;
import pers.hawk.room.local.HMsgService;
import pers.hawk.room.local.BeanMsgClient;
import pers.hawk.room.util.ByteStringUtil;
import pers.hawk.room.util.AppConst;
import pers.hawk.room.z.queue.entity.MsgContent;
import pers.hawk.room.z.queue.entity.Strategy;
import pers.hawk.room.z.queue.entity.StrategyTask;
import pers.hawk.room.z.queue.entity.Tactics;
import pers.hawk.room.z.security.BaseAES;
import pers.hawk.room.z.util.ClientSocket;
import pers.hawk.room.z.util.DateUtil;
import pers.hawk.room.z.util.SingletonFactory;
import pers.hawk.view.frame.TabMenu;

/**
 * 发送数据服务(主动消息)
 */
public class PushServer implements Runnable {

	public Logger logger = Logger.getLogger(this.getClass());
	public DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Map<String, TabMenu> mapTabMenu;
	public Map<String, ClientSocket> mapSocket;

	private PushServiceImp pushServiceImp;

	private HMsgService localMsgService;
	private MsgService msgService;
	private DeviceService deviceService;

	public PushServer() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		StringBuffer stringBuffer = null;
		try {
			while (true) {
				initSocket();
				synchronized (format) {
					try {
						format.wait(9 * 100);
					} catch (InterruptedException e1) {
						logger.warn(e1.toString());
					}
					format.notifyAll();
				}
			}
		} catch (Exception e) {
			logger.warn(e.toString());
			synchronized (mapTabMenu) {
				stringBuffer = mapTabMenu.get(Injection.menuString[3]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 发生错误,TCP服务发送数据到客户端失败.").append(AppConst.line);
				mapTabMenu.notifyAll();
			}
			synchronized (format) {
				try {
					format.wait(10 * 1000);
				} catch (InterruptedException e2) {
					logger.warn(e2.toString());
				}
				format.notifyAll();
			}
			initSocket();
		}
	}

	/**
	 * 
	 */
	public void initSocket() {

		Calendar calendar = Calendar.getInstance();

		listenMsg();

		synchronized (mapSocket) {
			Set<Map.Entry<String, ClientSocket>> set = mapSocket.entrySet();
			for (Iterator<Entry<String, ClientSocket>> iterator = set.iterator(); iterator.hasNext();) {
				Map.Entry<String, ClientSocket> entry = (Map.Entry<String, ClientSocket>) iterator.next();
				ClientSocket clientSocket = entry.getValue();

				List<BeanMsg> msgList = localMsgService.get(clientSocket.getBuilding_id() + clientSocket.getGateway_id());
				for (int j = 0; j < msgList.size(); j++) {
					List<BeanMsgClient> sendMsgClientList = parse(msgList.get(j), msgList);
					if (sendMsgClientList.size() > 0) {
						deviceService.updateDeviceStatu(sendMsgClientList);
						msgList.get(j).setLastCmd(format.format(calendar.getTime()));
						localMsgService.updateCmdTime(msgList.get(j));
					}
					sendMsg(clientSocket.getSocketChannel(), createMsg(clientSocket, sendMsgClientList));
				}
			}
		}
	}

	/**
	 * 监听消息并保存本地
	 */
	public void listenMsg() {
		StringBuffer stringBuffer = null;
		try {
			List<BeanMsg> list = null;
			list = msgService.getMsgList();
			if (list.size() > 0) {
				synchronized (mapTabMenu) {
					stringBuffer = mapTabMenu.get(Injection.menuString[2]).getStringBuffer();
					stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 接收到控制指令.").append(AppConst.line);
					mapTabMenu.notifyAll();
				}
			}
			localMsgService.adds(list);
			msgService.deleteMsgList(list);
		} catch (Exception e) {
			logger.warn(e.toString());
			synchronized (mapTabMenu) {
				stringBuffer = mapTabMenu.get(Injection.menuString[3]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 无法连接到控制指令服务器,请检查服务器是否开启.10秒后尝试重新连接").append(AppConst.line);
				stringBuffer.append(e.toString()).append(AppConst.line);
				mapTabMenu.notifyAll();
			}
			synchronized (format) {
				try {
					format.wait(10 * 1000);
				} catch (InterruptedException e2) {
					logger.warn(e2.toString());
				}
				format.notifyAll();
			}
			listenMsg();
		}
	}

	/**
	 * 生成消息
	 * 
	 * @param clientSockets
	 * @param cmdList
	 */
	public ByteBuffer createMsg(ClientSocket clientSockets, List<BeanMsgClient> cmdList) {

		StringBuffer stringBuffer = null;

		if (cmdList.size() < 1) {
			return null;
		}

		StringBuffer xml = new StringBuffer();

		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?><root><common><building_id>");
		xml.append(clientSockets.getBuilding_id());
		xml.append("</building_id><gateway_id>");
		xml.append(clientSockets.getGateway_id());
		xml.append("</gateway_id><type>meter_control</type></common><data operation=\"request\">");
		xml.append("<meters total=\"").append(cmdList.size()).append("\">");
		for (int i = 0; i < cmdList.size(); i++) {
			xml.append("<meter name=\"");
			xml.append(cmdList.get(i).getMeterCode());
			xml.append("\"><function>");
			xml.append(cmdList.get(i).getOperat());
			xml.append("</function></meter>");
		}
		xml.append("</meters></data></root>");

		byte[] byteHead = new byte[7];
		byteHead[0] = 0x1f;
		byteHead[1] = 0x1f;
		byteHead[2] = 0x5;

		byte[] bytes = null;
		try {
			bytes = BaseAES.encrypt(xml.toString().getBytes("UTF-8"));
		} catch (Exception e) {
		}

		ByteBuffer byteBuffer = stringToByteAES(bytes, byteHead);

		synchronized (mapTabMenu) {
			stringBuffer = mapTabMenu.get(Injection.menuString[2]).getStringBuffer();
			stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" TCP服务发送控制指令到客户端 ").append(AppConst.line);
			stringBuffer.append(ByteStringUtil.getByteString(bytes)).append(AppConst.line);
			stringBuffer.append(xml.toString()).append(AppConst.line);
			mapTabMenu.notifyAll();
		}

		return byteBuffer;
	}

	/**
	 * 发送客户端消息
	 * 
	 * @param socketChannel
	 * @param byteBuffer
	 */
	public void sendMsg(SocketChannel socketChannel, ByteBuffer byteBuffer) {
		StringBuffer stringBuffer = null;

		if (null == byteBuffer) {
			return;
		}

		try {
			byteBuffer.flip();
			while (byteBuffer.hasRemaining()) {
				socketChannel.write(byteBuffer);
			}
		} catch (IOException e) {
			synchronized (mapTabMenu) {
				stringBuffer = mapTabMenu.get(Injection.menuString[3]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 网络异常，发送数据到客户端失败，与客户端的连接断开。").append(AppConst.line);
				mapTabMenu.notifyAll();
			}
			logger.warn("\r\nIOException 发送数据到客户端失败:" + stringBuffer.toString() + e.getMessage() + "\r\n");

			// close socket?
			// closeClient(socketChannel);
			// close socket?
		}
	}

	/**
	 * String转Buffer
	 * 
	 * @param stringBuffer
	 * @return
	 */
	public ByteBuffer stringToByte(String string) {
		ByteBuffer byteBuffer = null;
		byte[] bytes = null;
		try {
			bytes = string.getBytes("UTF-8");
			byteBuffer = ByteBuffer.allocate(bytes.length);
			for (int i = 0; i < bytes.length; i++) {
				byteBuffer.put(bytes[i]);
			}
		} catch (UnsupportedEncodingException e) {
			logger.info(e);
			throw new RuntimeException(e);
		}
		return byteBuffer;
	}

	/**
	 * 分析控制命令
	 * 
	 * @param cmdQueueList
	 * @return
	 */
	public List<BeanMsgClient> parse(BeanMsg msg, List<BeanMsg> cmdQueues) {
		List<BeanMsgClient> SendMsgClientList = new ArrayList<BeanMsgClient>();

		MsgContent cmdMsg = JsonToObject(msg.getContent(), MsgContent.class);

		if (3 == cmdMsg.getType()) {
			Tactics tactics = JsonToObject(cmdMsg.getCommand(), Tactics.class);
			for (int a = 0; a < tactics.getTactics().length; a++) {
				BeanMsgClient clientCMD = new BeanMsgClient();
				SendMsgClientList.add(clientCMD);
				clientCMD.setMeterCode(tactics.getTactics()[a].getDeviceCode());
				clientCMD.setOperat(caseOper(tactics.getTactics()[a].getAction().getOrderValue()));
			}

			localMsgService.delete(msg.getQueueId());
			deviceService.updateDeviceStatu(SendMsgClientList);
		}
		// 策略
		else if (11 == cmdMsg.getType()) {

			Strategy strategy = null;
			strategy = JsonToObject(cmdMsg.getCommand(), Strategy.class);

			if (null != strategy.getStrategyTaskList()) {
				for (int i = 0; i < strategy.getStrategyTaskList().size(); i++) {
					if (invokeCmd(msg, strategy, strategy.getStrategyTaskList().get(i))) {
						BeanMsgClient sendClientCMD = new BeanMsgClient();
						SendMsgClientList.add(sendClientCMD);
						sendClientCMD.setMeterCode(strategy.getStrategyTaskList().get(i).getCode());
						sendClientCMD.setOperat(caseStrategyOper(String.valueOf(strategy.getStrategyTaskList().get(i).getStatus())));
					}
				}
			}
		}
		// 更新了此策略
		else if (21 == cmdMsg.getType()) {

			delcmd(msg, cmdMsg, cmdQueues);
			cmdMsg.setType(11);
			msg.setContent(ObjectToJson(cmdMsg));

			List<BeanMsg> list = new ArrayList<BeanMsg>();
			list.add(msg);

			localMsgService.adds(list);

		}
		// 删除了此策略
		else if (22 == cmdMsg.getType()) {
			delcmd(msg, cmdMsg, cmdQueues);
		}

		return SendMsgClientList;
	}

	/**
	 * 字符串转对象
	 * 
	 * @param content
	 * @param c
	 * @return
	 */
	public <C> C JsonToObject(String content, Class<C> c) {
		try {
			return SingletonFactory.getSingletonJson().readValue(content, c);
		} catch (Exception e) {
			logger.warn("\r\nException:" + e.getMessage() + "\r\n");
			throw new RuntimeException(e);
		}
	}

	/**
	 * 转换为tcp客户端识别的操作(单控制时使用)
	 * 
	 * @param string
	 * @return
	 */

	public String caseOper(String string) {
		String oper = "powercut";
		if (string.equals("1")) {
			oper = "electrify";
		} else if (Integer.parseInt(string) > 0) {
			oper = "electrify";
		}
		return oper;
	}

	/**
	 * 转换为tcp客户端识别的操作(策略设备控制时使用)
	 * 
	 * @param string
	 * @return
	 */
	public String caseStrategyOper(String string) {
		String oper = "powercut";
		if (string.equals("1")) {
			oper = "electrify";
		} else if (Integer.parseInt(string) > 0) {
			oper = "electrify";
		}
		return oper;
	}

	/**
	 * 是否在指定时间内（是否需要在当前时间内执行）
	 * 
	 * @param msg
	 * @param strategy
	 * @param ctlStrategyTask
	 * @return
	 */
	public boolean invokeCmd(BeanMsg msg, Strategy strategy, StrategyTask ctlStrategyTask) {
		boolean f = false;

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();

		DateUtil dateUtil = new DateUtil();
		dateUtil.setBeginString(ctlStrategyTask.getStartString());
		dateUtil.setEndString(ctlStrategyTask.getEndString());

		try {
			dateUtil.setBegin(format.parse(dateUtil.getBeginString()));
		} catch (ParseException e) {
			logger.warn("\r\nException:" + e.getMessage() + "\r\n");
			localMsgService.delete(msg.getQueueId());
		}

		int day = strategy.getScycle();

		Calendar beginCalendar = Calendar.getInstance();

		if (day == 0) {
			// 判断最后执行时间
			if (("1970-01-01 00:00:00".equals(msg.getLastCmd()))) {
				try {
					beginCalendar.setTime(format.parse(msg.getLastCmd()));
					if (f = isThisDate(beginCalendar)) {
						beginCalendar.setTime(dateUtil.getBegin());
						f = isThisTime(beginCalendar);
					}
				} catch (ParseException e) {
					logger.warn("\r\nException:" + e.getMessage() + "\r\n");
					localMsgService.delete(msg.getQueueId());
				}
			}

		} else if (day >= 1 && day < 7) {
			if (day == calendar.get(Calendar.DAY_OF_WEEK) - 1) {
				try {
					beginCalendar.setTime(format.parse(msg.getLastCmd()));
					if (f = isThisDate(beginCalendar)) {
						beginCalendar.setTime(dateUtil.getBegin());
						f = isThisTime(beginCalendar);
					}
				} catch (ParseException e) {
					logger.warn("\r\nException:" + e.getMessage() + "\r\n");
					localMsgService.delete(msg.getQueueId());
				}
			}
		} else if (day == 7) {
			if (1 == calendar.get(Calendar.DAY_OF_WEEK)) {
				try {
					beginCalendar.setTime(format.parse(msg.getLastCmd()));
					if (f = isThisDate(beginCalendar)) {
						beginCalendar.setTime(dateUtil.getBegin());
						f = isThisTime(beginCalendar);
					}
				} catch (ParseException e) {
					logger.warn("\r\nException:" + e.getMessage() + "\r\n");
					localMsgService.delete(msg.getQueueId());
				}
			}
		} else if (day == 8) {
			try {
				beginCalendar.setTime(format.parse(msg.getLastCmd()));
				if (f = isThisDate(beginCalendar)) {
					beginCalendar.setTime(dateUtil.getBegin());
					f = isThisTime(beginCalendar);
				}
			} catch (ParseException e) {
				logger.warn("\r\nException:" + e.getMessage() + "\r\n");
				localMsgService.delete(msg.getQueueId());
			}
		}

		return f;
	}

	/**
	 * @return
	 */
	public boolean isThisDate(Calendar beginCalendar) {
		boolean f = false;

		Calendar calendar = Calendar.getInstance();

		if (calendar.get(Calendar.YEAR) > beginCalendar.get(Calendar.YEAR)) {
			f = true;
		} else if (calendar.get(Calendar.YEAR) == beginCalendar.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) > beginCalendar.get(Calendar.MONTH)) {
			f = true;
		} else if (calendar.get(Calendar.YEAR) == beginCalendar.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == beginCalendar.get(Calendar.MONTH)
				&& calendar.get(Calendar.DAY_OF_MONTH) > beginCalendar.get(Calendar.DAY_OF_MONTH)) {
			f = true;
		}

		return f;
	}

	/**
	 * @return
	 */
	public boolean isThisTime(Calendar beginCalendar) {
		boolean f = false;

		Calendar calendar = Calendar.getInstance();

		if (calendar.get(Calendar.HOUR_OF_DAY) > beginCalendar.get(Calendar.HOUR_OF_DAY)) {
			f = true;
		} else if (calendar.get(Calendar.HOUR_OF_DAY) == beginCalendar.get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.MINUTE) >= beginCalendar.get(Calendar.MINUTE)) {
			f = true;
		}

		return f;
	}

	/**
	 * 将周期转为对应的星期
	 * 
	 * @return
	 */
	public int parseDayOfWeek(BeanMsg msg, Strategy strategy) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateUtil dateUtil = new DateUtil();
		dateUtil.setBeginString(strategy.getBeginTime());
		dateUtil.setEndString(strategy.getEndTime());

		try {
			dateUtil.setBegin(format.parse(strategy.getBeginTime()));
			dateUtil.setEnd(format.parse(strategy.getEndTime()));
		} catch (ParseException e) {
			logger.warn("\r\nException:" + e.getMessage() + "\r\n");
			localMsgService.delete(msg.getQueueId());
		}

		Calendar calendar = Calendar.getInstance();

		int day = strategy.getScycle();

		if (day == 0) {
			Calendar beginCalendar = Calendar.getInstance();
			Calendar endCalendar = Calendar.getInstance();
			beginCalendar.setTime(dateUtil.getBegin());
			endCalendar.setTime(dateUtil.getEnd());
			// 需要增加一个执行时间
			if (calendar.get(Calendar.HOUR_OF_DAY) >= beginCalendar.get(Calendar.HOUR_OF_DAY)) {
				if (msg.getLastCmd().length() < 1) {
					day = calendar.get(Calendar.DAY_OF_WEEK);
				}
			} else {
				day = 1 + calendar.get(Calendar.DAY_OF_WEEK);
			}
		} else if (day == 1) {
			// day = cycle;
		} else if (day == 2) {
			// day = cycle;
		} else if (day == 3) {
			// day = cycle;
		} else if (day == 4) {
			// day = cycle;
		} else if (day == 5) {
			// day = cycle;
		} else if (day == 6) {
			// day = cycle;
		} else if (day == 7) {
			// day = cycle;
		} else if (day == 8) {
			day = calendar.get(Calendar.DAY_OF_WEEK);
		} else if (day == 9) {

		}

		return day;
	}

	/**
	 * 删除本地策略
	 */
	public void delcmd(BeanMsg msg, MsgContent cmdMsg, List<BeanMsg> cmdQueues) {

		for (int i = 0; i < cmdQueues.size(); i++) {
			MsgContent tmpCmdMsg = JsonToObject(cmdQueues.get(i).getContent(), MsgContent.class);

			Strategy tmpStrategy = JsonToObject(tmpCmdMsg.getCommand(), Strategy.class);
			Strategy strategy = JsonToObject(cmdMsg.getCommand(), Strategy.class);

			if (tmpStrategy.getId() == strategy.getId()) {
				localMsgService.delete(cmdQueues.get(i).getQueueId());

				strategy.setId(tmpStrategy.getId());
				cmdMsg.setCommand(ObjectToJson(strategy));
			}
		}

	}

	/**
	 * 对象转字符串
	 * 
	 * @param object
	 * @return
	 */
	public String ObjectToJson(Object object) {
		try {
			return SingletonFactory.getSingletonJson().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			logger.warn("\r\nException:" + e.getMessage() + "\r\n");
			throw new RuntimeException(e);
		}
	}

	/**
	 * String转Buffer AES
	 * 
	 * @param string
	 * @param byteHead
	 * @return
	 */
	public ByteBuffer stringToByteAES(byte[] bytes, byte[] byteHead) {

		ByteBuffer byteBuffer = null;
		byteBuffer = ByteBuffer.allocate(bytes.length + 7);

		for (int i = 0; i < 3; i++) {
			byteBuffer.put(byteHead[i]);
		}
		byte[] byteLength = ByteStringUtil.int2Bytes(bytes.length, 4);
		for (int i = 0; i < byteLength.length; i++) {
			byteBuffer.put(byteLength[i]);
		}

		for (int i = 0; i < bytes.length; i++) {
			byteBuffer.put(bytes[i]);
		}
		return byteBuffer;
	}

	public PushServiceImp getPushServiceImp() {
		return pushServiceImp;
	}

	public void setPushServiceImp(PushServiceImp pushServiceImp) {
		this.pushServiceImp = pushServiceImp;
	}

	public HMsgService getLocalMsgService() {
		return localMsgService;
	}

	public void setLocalMsgService(HMsgService localMsgService) {
		this.localMsgService = localMsgService;
	}

	public MsgService getMsgService() {
		return msgService;
	}

	public void setMsgService(MsgService msgService) {
		this.msgService = msgService;
	}

	public DeviceService getDeviceService() {
		return deviceService;
	}

	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

}

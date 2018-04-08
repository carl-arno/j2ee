package pers.hawk.room.server;

import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import pers.hawk.room.Injection;
import pers.hawk.room.dbserver.DeviceService;
import pers.hawk.room.dbserver.EnergyService;
import pers.hawk.room.local.BeanMsgClient;
import pers.hawk.room.local.HService;
import pers.hawk.room.util.AppConst;
import pers.hawk.room.util.ByteStringUtil;
import pers.hawk.room.z.security.AESConstant;
import pers.hawk.room.z.security.BaseAES;
import pers.hawk.room.z.util.ClientSocket;
import pers.hawk.room.z.util.SingletonFactory;
import pers.hawk.room.z.xml.entity.Common;
import pers.hawk.room.z.xml.entity.Data;
import pers.hawk.room.z.xml.entity.EnergyItem;
import pers.hawk.room.z.xml.entity.EnergyItems;
import pers.hawk.room.z.xml.entity.HeartBeat;
import pers.hawk.room.z.xml.entity.IdValidate;
import pers.hawk.room.z.xml.entity.Meter;
import pers.hawk.room.z.xml.entity.Meters;
import pers.hawk.room.z.xml.entity.Root1;
import pers.hawk.room.z.xml.entity.Root2;
import pers.hawk.room.z.xml.entity.Root3;
import pers.hawk.view.frame.TabMenu;

/**
 * 业务层
 */
public class ReceiveServiceImp {

	private Logger logger = Logger.getLogger(this.getClass());
	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Map<String, TabMenu> mapTabMenu;

	public Map<String, ClientSocket> mapSocket;

	private HService localService;

	private EnergyService energyService;

	public ReceiveServiceImp() {
		super();
	}

	/**
	 * 0x1：身份认证，Data 体是明文数据
	 */
	public Object clientVerify(SocketChannel socketChannel, byte[] bodyByte) {

		StringBuffer stringBuffer = null;

		StringBuffer xml = new StringBuffer();
		String bodyString = ByteStringUtil.getBodyString(bodyByte);

		Root1 root1 = null;
		Root1 respRoot1 = null;

		root1 = XMLToObject(bodyString, Root1.class);
		respRoot1 = new Root1();

		ClientSocket clientSocket = new ClientSocket();
		clientSocket.setBuilding_id(root1.getCommon().getBuilding_id());
		clientSocket.setGateway_id(root1.getCommon().getGateway_id());
		clientSocket.setSocketChannel(socketChannel);

		respRoot1.setCommon(new Common());
		respRoot1.setId_validate(new IdValidate());

		// 判断是否为请求
		if (null == root1.getId_validate().getMd5()) {
			// 发送随机序列
			Random random = new Random();
			String[] tmps = String.valueOf(random.nextDouble()).split("\\.");
			tmps[1] = tmps[1].substring(0, 8);

			clientSocket.setKey(AESConstant.getKEYSTRING() + tmps[1]);
			mapSocket.put(root1.getCommon().getBuilding_id(), clientSocket);

			respRoot1.getId_validate().setSequence(tmps[1]);

			xml.setLength(0);
			xml.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?><root><common><building_id>");
			xml.append(root1.getCommon().getBuilding_id());
			xml.append("</building_id><gateway_id>");
			xml.append(root1.getCommon().getGateway_id());
			xml.append("</gateway_id><type>");
			xml.append(root1.getCommon().getType());
			xml.append("</type></common><id_validate operation=\"sequence\"><sequence>");
			xml.append(respRoot1.getId_validate().getSequence());
			xml.append("</sequence></id_validate></root>");

			synchronized (mapTabMenu) {
				stringBuffer = mapTabMenu.get(Injection.menuString[1]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 客户端数据 {身份认证}.").append(AppConst.line);
				stringBuffer.append(bodyString).append(AppConst.line);
				stringBuffer.append("TCP服务发送随机序列.").append(AppConst.line);
				stringBuffer.append(xml.toString()).append(AppConst.line);
				mapTabMenu.notifyAll();
			}

		} else if (null != root1.getId_validate().getMd5()) {

			clientSocket = mapSocket.get(root1.getCommon().getBuilding_id());

			if (root1.getId_validate().getMd5().equals(DigestUtils.md5Hex(clientSocket.getKey()))) {
				respRoot1.getId_validate().setResult("pass");
			} else {
				respRoot1.getId_validate().setResult("fail");
			}

			xml.setLength(0);
			xml.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?><root><common><building_id>");
			xml.append(root1.getCommon().getBuilding_id());
			xml.append("</building_id><gateway_id>");
			xml.append(root1.getCommon().getGateway_id());
			xml.append("</gateway_id><type>");
			xml.append(root1.getCommon().getType());
			xml.append("</type></common><id_validate operation=\"result\"><result>");
			xml.append(respRoot1.getId_validate().getResult());
			xml.append("</result></id_validate></root>");

			synchronized (mapTabMenu) {
				stringBuffer = mapTabMenu.get(Injection.menuString[1]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 客户端数据 {认证密钥}").append(AppConst.line);
				stringBuffer.append(bodyString).append(AppConst.line);
				stringBuffer.append("TCP服务发送验证结果.").append(AppConst.line);
				stringBuffer.append(xml.toString()).append(AppConst.line);
				mapTabMenu.notifyAll();
			}

		} else {
			// throw new RuntimeException("未知错误.");
		}

		return xml.toString();
	}

	/**
	 * 0x2：心跳信息，Data 体是明文数据
	 */
	public Object clientKeep(SocketChannel socketChannel, byte[] bodyByte) {

		StringBuffer stringBuffer = null;

		StringBuffer xml = new StringBuffer();
		String bodyString = ByteStringUtil.getBodyString(bodyByte);

		Root2 root2 = null;
		Root2 respRoot2 = null;
		root2 = XMLToObject(bodyString, Root2.class);
		respRoot2 = new Root2();

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		respRoot2.setHeart_beat(new HeartBeat());
		respRoot2.getHeart_beat().setTime(format.format(Calendar.getInstance().getTime()));

		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>").append("<root>").append("<common>").append("<building_id>");
		xml.append(root2.getCommon().getBuilding_id());
		xml.append("</building_id>").append("<gateway_id>");
		xml.append(root2.getCommon().getGateway_id());
		xml.append("</gateway_id>").append("<type>");
		xml.append(root2.getCommon().getType());
		xml.append("</type></common><heart_beat operation=\"time\"><time>");
		xml.append(respRoot2.getHeart_beat().getTime());
		xml.append("</time></heart_beat></root>");

		synchronized (mapTabMenu) {
			stringBuffer = mapTabMenu.get(Injection.menuString[1]).getStringBuffer();
			stringBuffer.append(this.format.format(Calendar.getInstance().getTime())).append(" 客戶端数据 {心跳或校时}").append(AppConst.line);
			stringBuffer.append(bodyString).append(AppConst.line);
			stringBuffer.append("TCP服务发送系统时间.").append(AppConst.line);
			stringBuffer.append(xml.toString()).append(AppConst.line);
			mapTabMenu.notifyAll();
		}

		return xml.toString();
	}

	/**
	 * 0x3：能耗数据,Data 体是通过AES 加密后的数据
	 */
	@SuppressWarnings("unchecked")
	public Object clientData(SocketChannel socketChannel, byte[] bodyByte) {

		StringBuffer stringBuffer = null;

		StringBuffer xml = new StringBuffer();
		String bodyString = BaseAES.decrypt(bodyByte);

		synchronized (mapTabMenu) {
			stringBuffer = mapTabMenu.get(Injection.menuString[1]).getStringBuffer();
			stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 客戶端数据 {能耗数据上传}").append(AppConst.line);
			stringBuffer.append(bodyString).append(AppConst.line);
			mapTabMenu.notifyAll();
		}

		Root3 root3 = new Root3();
		root3.setBranchId(0);
		root3.setData(new Data());
		root3.setCommon(new Common());
		root3.getData().setMeters(new Meters());
		root3.getData().setEnergyItems(new EnergyItems());
		root3.getData().getMeters().setMeter(new ArrayList<Meter>());
		root3.getData().getEnergyItems().setEnergyItems(new ArrayList<EnergyItem>());

		Document doc = parseToDoc(bodyString);

		Element rootElement = doc.getRootElement(); // 获取根节点
		// Element common = rootElement.element("common");
		Element data = rootElement.element("data");
		Element time = data.element("time");
		Element energyItems = data.element("energy_items");
		Element meters = data.element("meters");
		Iterator<Element> energyItem = energyItems.elementIterator("energy_item");
		Iterator<Element> meter = meters.elementIterator("meter");

		SimpleDateFormat tmpFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		root3.getCommon().setBuilding_id(rootElement.element("common").element("building_id").getTextTrim());
		root3.getCommon().setGateway_id(rootElement.element("common").element("gateway_id").getTextTrim());
		root3.getCommon().setType(rootElement.element("common").element("type").getTextTrim());

		try {
			root3.getData().setTime(format.format(tmpFormat.parse(time.getTextTrim())));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		try {
			while (energyItem.hasNext()) {
				EnergyItem tmp = new EnergyItem();
				root3.getData().getEnergyItems().getEnergyItems().add(tmp);

				Element element2 = (Element) energyItem.next();
				tmp.setCode(element2.attribute("code").getText().trim());
				tmp.setValue(Double.parseDouble(element2.getTextTrim()));
			}
			while (meter.hasNext()) {
				Meter tmp = new Meter();
				root3.getData().getMeters().getMeter().add(tmp);

				Element eMeter = (Element) meter.next();
				Element eFunction = eMeter.element("function");

				tmp.setId(eMeter.attribute("id").getText().trim());
				//
				root3.setBranchId(Long.parseLong(energyService.getMeterByCode(tmp.getId()).get(0)[3]));
				//

				tmp.setName(eMeter.attribute("name").getText().trim());
				tmp.setFunction(eFunction.attribute("id").getText().trim());
				tmp.setErrror(eFunction.attribute("error").getText().trim());
				tmp.setValue(Double.parseDouble(eFunction.getTextTrim()));
			}

			xml.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?><root><common><building_id>");
			xml.append(root3.getCommon().getBuilding_id());
			xml.append("</building_id><gateway_id>");
			xml.append(root3.getCommon().getGateway_id());
			xml.append("</gateway_id><type>");
			xml.append(root3.getCommon().getType());
			xml.append("</type></common><data operation=\"report\"><time>");
			xml.append(time.getTextTrim());
			xml.append("</time><ack>OK</ack></data></root>");

			energyService.add(root3);
			synchronized (mapTabMenu) {
				stringBuffer = mapTabMenu.get(Injection.menuString[1]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" TCP服务保存到数据库成功.").append(AppConst.line);
				mapTabMenu.notifyAll();
			}

		} catch (Exception e) {
			xml.setLength(0);
			xml.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?><root><common><building_id>");
			xml.append(root3.getCommon().getBuilding_id());
			xml.append("</building_id><gateway_id>");
			xml.append(root3.getCommon().getGateway_id());
			xml.append("</gateway_id><type>");
			xml.append(root3.getCommon().getType());
			xml.append("</type></common><data operation=\"report\"><time>");
			xml.append(time.getTextTrim());
			xml.append("</time><ack>fail…</ack></data></root>");

			synchronized (mapTabMenu) {
				stringBuffer = mapTabMenu.get(Injection.menuString[1]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" TCP服务保存到数据库失败.").append(AppConst.line);
				mapTabMenu.notifyAll();
			}
		}

		synchronized (mapTabMenu) {
			stringBuffer = mapTabMenu.get(Injection.menuString[1]).getStringBuffer();
			stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" TCP服务发送能耗数据上传结果").append(AppConst.line);
			stringBuffer.append(xml.toString()).append(AppConst.line);
			mapTabMenu.notifyAll();
		}

		return xml.toString();
	}

	/**
	 * 0x5: 控制命令结果,Data 体是通过AES 加密后的数据
	 * 
	 * @param bytes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object clientResult(SocketChannel socketChannel, byte[] bodyByte) {

		StringBuffer stringBuffer = null;

		String bodyString = BaseAES.decrypt(bodyByte);

		stringBuffer = mapTabMenu.get(Injection.menuString[2]).getStringBuffer();
		synchronized (mapTabMenu) {
			stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 客戶端数据 {指令执行结果}").append(AppConst.line);
			stringBuffer.append(bodyString).append(AppConst.line);
			mapTabMenu.notifyAll();
		}

		Document doc = null;
		doc = parseToDoc(bodyString);

		Element rootElement = doc.getRootElement(); //
		Element data = rootElement.element("data");
		Element meters = data.element("meters");
		Iterator<Element> meter = meters.elementIterator("meter");

		List<BeanMsgClient> beanMsgClientList = new ArrayList<BeanMsgClient>();

		try {
			while (meter.hasNext()) {
				BeanMsgClient beanMsgClient = new BeanMsgClient();

				Element eMeter = (Element) meter.next();

				Element eControlState = eMeter.element("control_state");
				Element eFunction = eMeter.element("function");

				beanMsgClient.setMeterCode(eMeter.attribute("name").getText().trim());
				beanMsgClient.setOperat(eFunction.getText().trim());

				if (eControlState.getText().trim().equals("ok")) {
					beanMsgClientList.add(beanMsgClient);
				}
			}

			deviceService.updateDeviceStatu(beanMsgClientList);
			synchronized (mapTabMenu) {
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 更新状态成功.").append(AppConst.line);
				mapTabMenu.notifyAll();
			}
		} catch (Exception e) {
			synchronized (mapTabMenu) {
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 更新状态失败.").append(e.toString()).append(AppConst.line);
				mapTabMenu.notifyAll();
			}
		}

		return null;
	}

	/**
	 * 0x6: 电流电压,Data 体是通过AES 加密后的数据
	 * 
	 * @param bytes
	 * @return
	 */
	public Object client(SocketChannel socketChannel, byte[] bodyByte) {
		return null;
	}

	private DeviceService deviceService;

	public DeviceService getDeviceService() {
		return deviceService;
	}

	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	/**
	 * 其他：不允许接入
	 */
	public Object clientDeprate(SocketChannel socketChannel, byte[] bodyByte) {

		StringBuffer stringBuffer = null;

		StringBuffer xml = new StringBuffer();
		try {
			String bodyString = ByteStringUtil.getBodyString(bodyByte);

			xml.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");

			synchronized (mapTabMenu) {
				stringBuffer = mapTabMenu.get(Injection.menuString[1]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 检测到其他请求数据.").append(AppConst.line);
				stringBuffer.append(bodyString).append(AppConst.line);
				mapTabMenu.notifyAll();
			}
		} catch (Exception e) {
			logger.warn(e.toString());
		}

		return xml.toString();
	}

	/**
	 * 將字符串转为对象
	 */
	public <C> C XMLToObject(String content, Class<C> c) {
		return SingletonFactory.getSingletonXML().readValue(content, c);
	}

	/**
	 * Document 解析 xml
	 */
	public Document parseToDoc(String content) {
		try {
			Document doc = null;
			doc = DocumentHelper.parseText(content.trim());
			return doc;
		} catch (DocumentException e) {
			logger.warn("\r\n: " + e.getMessage() + "\r\n");
			throw new RuntimeException(e);
		}
	}

	public HService getLocalService() {
		return localService;
	}

	public void setLocalService(HService localService) {
		this.localService = localService;
	}

	public Map<String, ClientSocket> getMapSocket() {
		return mapSocket;
	}

	public void setMapSocket(Map<String, ClientSocket> mapSocket) {
		this.mapSocket = mapSocket;
	}

	public EnergyService getEnergyService() {
		return energyService;
	}

	public void setEnergyService(EnergyService energyService) {
		this.energyService = energyService;
	}

}

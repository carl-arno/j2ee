package pers.hawk.room.dbserver;

import java.util.ArrayList;
import java.util.List;

import pers.hawk.room.local.HMsgService;

/**
 * 消息队列（业务层）
 */
public class MsgService extends JDBCMain {

	private HMsgService msgService;

	private String id;

	public void setId(String id) {
		this.id = id;
	}

	public pers.hawk.room.local.HMsgService getMsgService() {
		return msgService;
	}

	public void setMsgService(pers.hawk.room.local.HMsgService msgService) {
		this.msgService = msgService;
	}

	public String getId() {
		return id;
	}

	/**
	 * 获得当前控制器的控制指令
	 * 
	 * @return
	 */
	public List<BeanMsg> getMsgList() {

		List<BeanMsg> list = new ArrayList<BeanMsg>();

		String sql = "";

//		sql = " select QueueID,ControlDeviceGUID,OrderJSON from DeviceControlQueue where ControlDeviceGUID='" + id + "'; ";

		sql = " select QueueID,ControlDeviceGUID,OrderJSON from DeviceControlQueue ; ";

		List<String[]> stringList = select(sql);

		for (int i = 0; i < stringList.size(); i++) {
			BeanMsg cmdQueue = new BeanMsg();
			list.add(cmdQueue);

			cmdQueue.setQueueId(Long.parseLong(stringList.get(i)[0]));
			cmdQueue.setCode(stringList.get(i)[1]);
			cmdQueue.setContent(stringList.get(i)[2]);
		}

		return list;
	}

	public void deleteMsgList(List<BeanMsg> list) {

		StringBuffer stringBuffer = new StringBuffer();

		if (list.size() < 1) {
			return;
		}

		stringBuffer.append(" delete from DeviceControlQueue where queueid in (");
		for (int i = 0; i < list.size(); i++) {
			stringBuffer.append(list.get(i).getQueueId());
			stringBuffer.append(",");
		}
		stringBuffer.deleteCharAt(stringBuffer.length() - 1);
		stringBuffer.append("); ");

		update(stringBuffer.toString());
	}

	// /**
	// * @return
	// */
	// public List<CMDQueue> getLocalCMDList() {
	//
	// List<CMDQueue> list = new ArrayList<CMDQueue>();
	//
	// String sql = "  SELECT * FROM CMDCONTENT where code='' ;  ";
	//
	// List<String[]> stringList = select(sql);
	//
	// for (int i = 0; i < stringList.size(); i++) {
	// CMDQueue cmdQueue = new CMDQueue();
	// list.add(cmdQueue);
	//
	// cmdQueue.setQueueId(Long.parseLong(stringList.get(i)[0]));
	// cmdQueue.setContent(stringList.get(i)[2]);
	// }
	//
	// return list;
	// }

	// public CMDContent[] parseToObject(List<Msg> cmdQueueList) {
	// ObjectMapper mapper = SingletonFactory.getSingletonJson();
	//
	// CMDContent cmdContent;
	//
	// for (int i = 0; i < cmdQueueList.size(); i++) {
	// try {
	// cmdContent = mapper.readValue(cmdQueueList.get(i).getContent(),
	// CMDContent.class);
	// cmdContent.setCommand_(mapper.readValue(cmdContent.getCommand(),
	// Command.class));
	// } catch (Exception e) {
	// throw new RuntimeException(e);
	// }
	// }
	// return null;
	// }

	// test

	// public void sendCMD(List<CMDQueue> list) throws JsonParseException,
	// JsonMappingException, IOException {
	// CMDContent cmdContent = null;
	// if (list.size() > 0) {
	// CMDQueue cmdQueue = list.get(0);
	// cmdContent = toObject(cmdQueue);
	// }
	// System.out.println(cmdContent.hashCode());
	// }
	//
	// public CMDContent toObject(CMDQueue cmdQueue) throws JsonParseException,
	// JsonMappingException, IOException {
	// ObjectMapper mapper = Injection.getSingletonJson();
	// return mapper.readValue(cmdQueue.getContent(), CMDContent.class);
	// }

}

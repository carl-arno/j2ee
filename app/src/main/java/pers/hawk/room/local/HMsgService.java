package pers.hawk.room.local;

import java.util.ArrayList;
import java.util.List;

import pers.hawk.room.dbserver.BeanMsg;

/**
 * 控制设备内容(service层)
 *
 */
public class HMsgService extends JDBCH {

	public void adds(List<BeanMsg> queueList) {

		if (queueList.size() < 1) {
			return;
		}
		StringBuffer stringBuffer = new StringBuffer();

		for (int i = 0; i < queueList.size(); i++) {
			stringBuffer.append(" insert into CMDCONTENT(id,code,name,lastcmd) values( ");
			stringBuffer.append(queueList.get(i).getQueueId());
			stringBuffer.append(" , '");
			stringBuffer.append(queueList.get(i).getCode());
			stringBuffer.append("' ,'");
			stringBuffer.append(queueList.get(i).getContent());
			stringBuffer.append("' ,'1970-01-01 00:00:00");
			stringBuffer.append("'); ");
		}

		super.add(stringBuffer.toString());
	}

	public void delete(long id) {
		String sql = " delete from CMDCONTENT where id = '" + id + "'";
		super.update(sql);
	}

	/**
	 * 修改cmd执行的时间
	 * 
	 * @param queueList
	 */
	public void updateCmdSTime(List<BeanMsg> queueList) {

		if (queueList.size() < 1) {
			return;
		}

		StringBuffer stringBuffer = new StringBuffer();

		for (int i = 0; i < queueList.size(); i++) {
			stringBuffer.append(" update CMDCONTENT set cmdTime= '");
			stringBuffer.append(queueList.get(i).getLastCmd());
			stringBuffer.append("' where id = '");
			stringBuffer.append(queueList.get(i).getQueueId());
			stringBuffer.append("' ; ");
		}

		super.update(stringBuffer.toString());
	}

	/**
	 * 修改cmd执行的时间
	 * 
	 * @param queue
	 */
	public void updateCmdTime(BeanMsg queue) {

		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append(" update CMDCONTENT set lastcmd= '");
		stringBuffer.append(queue.getLastCmd());
		stringBuffer.append("' where id = '");
		stringBuffer.append(queue.getQueueId());
		stringBuffer.append("' ; ");

		super.update(stringBuffer.toString());
	}

	public List<BeanMsg> get(String code) {
		List<BeanMsg> list = new ArrayList<BeanMsg>();

		String sql = "  SELECT * FROM CMDCONTENT where code='" + code + "' ;  ";

		List<String[]> stringList = select(sql);

		for (int i = 0; i < stringList.size(); i++) {
			BeanMsg cmdQueue = new BeanMsg();
			list.add(cmdQueue);

			cmdQueue.setQueueId(Long.parseLong(stringList.get(i)[0]));
			cmdQueue.setCode(stringList.get(i)[1]);
			cmdQueue.setContent(stringList.get(i)[2]);
			cmdQueue.setLastCmd(stringList.get(i)[3]);
		}

		return list;
	}

	public void deleteAll() {
		String sql = " delete from CMDCONTENT ;";
		super.update(sql);
	}

}

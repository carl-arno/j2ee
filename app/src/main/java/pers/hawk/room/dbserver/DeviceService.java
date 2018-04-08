package pers.hawk.room.dbserver;

import java.util.List;

import org.apache.log4j.Logger;

import pers.hawk.room.local.BeanMsgClient;

public class DeviceService extends JDBCMain {

	Logger log=Logger.getLogger(this.getClass());
	
	
	/**
	 * 更新设备状态
	 */
	public void updateDeviceStatu(List<BeanMsgClient> clientCMDList) {
		StringBuffer stringBuffer = new StringBuffer();

		for (int i = 0; i < clientCMDList.size(); i++) {
			String onOff="";
			
			if ("powercut".equals(clientCMDList.get(i).getOperat())) {
				onOff="OFF";
			} else if ("electrify".equals(clientCMDList.get(i).getOperat())) {
				onOff="ON";
			}

			stringBuffer.append(" update device set DevStatus ='");
			stringBuffer.append(onOff);
			stringBuffer.append("' where devicecode ='");
			stringBuffer.append(clientCMDList.get(i).getMeterCode());
			stringBuffer.append("';");
		}

		log.info(stringBuffer.toString());
		
		update(stringBuffer.toString());

	}

}

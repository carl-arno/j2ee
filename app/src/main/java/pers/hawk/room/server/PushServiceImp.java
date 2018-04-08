package pers.hawk.room.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.log4j.Logger;

import pers.hawk.room.local.HService;
import pers.hawk.room.z.util.ClientSocket;
import pers.hawk.view.frame.TabMenu;

/**
 * 业务层
 */
@SuppressWarnings("unused")
public class PushServiceImp {

	private Logger logger = Logger.getLogger(this.getClass());
	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Map<String, TabMenu> mapTabMenu;

	public Map<String, ClientSocket> mapSocket;

	private HService localService;
	
	public PushServiceImp() {
		super();
	}
	
	public HService getLocalService() {
		return localService;
	}

	public void setLocalService(HService localService) {
		this.localService = localService;
	}
	
	
	
	
	
}

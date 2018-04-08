package pers.hawk.room.local;

import java.util.ArrayList;
import java.util.List;

import org.h2.jdbcx.JdbcConnectionPool;

/**
 * 本地数据访问(service层)
 */
public class HService extends JDBCH {

	public HService(JdbcConnectionPool jdbcConnectionPool) {
		super(jdbcConnectionPool);
	}

	/**
	 * 是否首次启动程序(true:是)
	 */
	public boolean getIsFirst() {
		boolean b = false;
		List<String[]> strings = select(" SELECT * FROM CLIENT where code='config' ");
		if (!"false".equals(strings.get(0)[3])) {
			b = true;
		}
		return b;
	}

	/**
	 * @param code
	 * @return
	 */
	public String[] getClientByCode(String code) {
		List<String[]> strings = select(" SELECT * FROM CLIENT where code ='" + code + "' ");
		return strings.get(0);
	}

	/**
	 * @param code
	 * @return
	 */
	public String[] getTCPClient(String code) {
		List<String[]> strings = select(" SELECT * FROM TCPCLIENT where code ='" + code + "' ");
		return strings.get(0);
	}

	/**
	 * @return
	 */
	public List<BeanDBUri> getDataBaseConfigList() {
		List<String[]> strings = select("SELECT * FROM REMOTEDATEBASE order by id");

		List<BeanDBUri> dataBaseConfigList = new ArrayList<BeanDBUri>();
		BeanDBUri dbConfig = null;
		for (String[] strings2 : strings) {
			dbConfig = new BeanDBUri();
			dbConfig.setId(strings2[0]);
			dbConfig.setName(strings2[1]);
			dbConfig.setUrl(strings2[2]);
			dbConfig.setDriverClassName(strings2[3]);
			dbConfig.setUsername(strings2[4]);
			dbConfig.setPassword(strings2[5]);

			dataBaseConfigList.add(dbConfig);
		}
		return dataBaseConfigList;
	}

	/**
	 * 获得数据库配置地址
	 *
	 */
	public BeanDBUri getDataBaseConfigByName(String name) {
		String[] strings = select("SELECT * FROM REMOTEDATEBASE where NAME ='" + name + "';").get(0);

		BeanDBUri dbConfig = null;
		dbConfig = new BeanDBUri();
		dbConfig.setId(strings[0]);
		dbConfig.setName(strings[1]);
		dbConfig.setUrl(strings[2]);
		dbConfig.setDriverClassName(strings[3]);
		dbConfig.setUsername(strings[4]);
		dbConfig.setPassword(strings[5]);

		return dbConfig;
	}

	// /**
	// */
	// public DelayH2 getDelay() {
	// String[] stringsDelay =
	// select(" SELECT * FROM DELAY order by id").get(0);
	//
	// DelayH2 delay = new DelayH2();
	// delay.setLoadData(Integer.parseInt(stringsDelay[1]));
	// delay.setControlDevice(Integer.parseInt(stringsDelay[2]));
	//
	// return delay;
	// }
	//
	// /**
	// */
	// public Client getDeploy() {
	// String[] stringsDeploy =
	// select(" SELECT * FROM CLIENT where CODE = 'deploy' ").get(0);
	//
	// Client client = new Client();
	// client.setName(stringsDeploy[2]);
	// client.setValue(stringsDeploy[3]);
	//
	// return client;
	// }

	/**
	 */
	public void updateModel(String sql) {
		super.update(sql);
	}

	public List<String[]> getModel() {
		List<String[]> stringsList = super.select(" select * from CLIENT where CODE='deploy' ; ");
		return stringsList;
	}

	/**
	 * 获得控制器标识
	 */
	public List<String[]> getCMDId() {
		List<String[]> stringsList = new ArrayList<String[]>();
		stringsList.add(new String[] { "MsgCMD" });
		return stringsList;
	}

}

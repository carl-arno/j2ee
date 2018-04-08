package pers.hawk.room.dbserver;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pers.hawk.room.local.BeanDBUri;
import pers.hawk.room.local.HService;

/**
 * 主数据库（dao）
 */
public class JDBCMain {

	private Logger logger = Logger.getLogger(this.getClass());

	private HService localService;

	private Connection connection;

	/**
	 * 
	 */
	public JDBCMain() {
		super();
	}

	public HService getLocalService() {
		return localService;
	}

	public void setLocalService(HService localService) {
		this.localService = localService;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * 尝试重新连接数据库
	 */
	private void initDB() {
		BeanDBUri dbUri = this.localService.getDataBaseConfigByName("web");
		try {
			Class.forName(dbUri.getDriverClassName());
			connection = DriverManager.getConnection(dbUri.getUrl(), dbUri.getUsername(), dbUri.getPassword());
		} catch (ClassNotFoundException e) {
			logger.error(e.toString());
			throw new RuntimeException(e);
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param string
	 * @return
	 */
	public long add(String string) {
		if (null == connection) {
			initDB();
		}

		long i = 0;

		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(10000);
			i = statement.executeUpdate(string);
		} catch (SQLException e) {
			logger.warn(e.toString());
			connection = null;
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				logger.warn(e.toString());
			}
		}
		return i;
	}

	/**
	 * 存储过程增加 异常抛出
	 * 
	 * @throws SQLException
	 */
	public String addWithProc(String sql, Object[] objects) {
		if (null == connection) {
			initDB();
		}

		String string = "";
		CallableStatement callableStatement = null;
		try {
			callableStatement = connection.prepareCall(sql);

			callableStatement.setQueryTimeout(1000 * 10);
			for (int i = 0; i < objects.length; i++) {
				callableStatement.setObject(i + 1, objects[i]);
			}
			callableStatement.execute();

		} catch (SQLException e) {
			logger.warn(e.toString());
			connection = null;
		} finally {
			try {
				callableStatement.close();
			} catch (SQLException e) {
				logger.warn(e.toString());
			}
		}

		return string;
	}

	/**
	 * @param string
	 * @return
	 * @throws SQLException
	 */
	public int update(String string) {
		if (null == connection) {
			initDB();
		}
		int i = 0;
		Statement statement = null;
		try {
			statement = connection.createStatement();

			statement.setQueryTimeout(10000);
			i = statement.executeUpdate(string);

		} catch (SQLException e) {
			logger.warn(e.toString());
			connection = null;
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				logger.warn(e.toString());
			}
		}
		return i;
	}

	/**
	 * @param string
	 * @return
	 * @throws SQLException
	 */
	public List<String[]> select(String string) {
		if (null == connection) {
			initDB();
		}
		
		List<String[]> list = new ArrayList<String[]>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(10000);
			ResultSet resultSet = statement.executeQuery(string);
			while (resultSet.next()) {
				String[] strings = new String[resultSet.getMetaData().getColumnCount()];
				for (int i = 0; i < strings.length; i++) {
					strings[i] = resultSet.getString(i + 1);
				}
				list.add(strings);
			}
		} catch (SQLException e) {
			logger.warn(e.toString());
			connection = null;
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				logger.warn(e.toString());
			}
		}

		return list;
	}

}

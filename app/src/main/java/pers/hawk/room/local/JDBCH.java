package pers.hawk.room.local;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.h2.jdbcx.JdbcConnectionPool;

/**
 * 本地数据库
 */
public class JDBCH {

	private JdbcConnectionPool jdbcConnectionPool;

	public JdbcConnectionPool getJdbcConnectionPool() {
		return jdbcConnectionPool;
	}

	public void setJdbcConnectionPool(JdbcConnectionPool jdbcConnectionPool) {
		this.jdbcConnectionPool = jdbcConnectionPool;
	}

	/**
	 */
	public JDBCH() {
	}

	/**
	 * @param jdbcConnectionPool
	 */
	public JDBCH(JdbcConnectionPool jdbcConnectionPool) {
		super();
		this.jdbcConnectionPool = jdbcConnectionPool;
	}

	public int add(String string) {

		Connection connection = null;
		Statement statement = null;
		int row = 0;

		try {
			connection = jdbcConnectionPool.getConnection();
			statement = connection.createStatement();
			row = statement.executeUpdate(string);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		return row;
	}

	/**
	 * @param string
	 * @return
	 */
	public int update(String string) {

		Connection connection = null;
		Statement statement = null;
		int row = 0;

		try {
			connection = jdbcConnectionPool.getConnection();
			statement = connection.createStatement();
			row = statement.executeUpdate(string);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		return row;
	}

	/**
	 * @param string
	 * @return
	 * @throws SQLException
	 */
	public List<String[]> select(String string) {

		List<String[]> list = new ArrayList<String[]>();
		String[] strings = null;

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = jdbcConnectionPool.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(string);
			while (resultSet.next()) {
				strings = new String[resultSet.getMetaData().getColumnCount()];
				for (int i = 0; i < strings.length; i++) {
					strings[i] = resultSet.getString(i + 1);
				}
				list.add(strings);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		return list;
	}

}

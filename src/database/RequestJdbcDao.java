package database;

import java.text.SimpleDateFormat;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import status.StatusCommand;

public class RequestJdbcDao {
	private String jdbcDriver;
	private String dbUrl;
	private String user;
	private String pass;
	private Connection conn = null;
	private Statement stmt = null;

	private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `statistic` "
			+ "(`id` int(11) NOT NULL AUTO_INCREMENT, "
			+ "`src_ip` varchar(256) NOT NULL, "
			+ "`uri` varchar(256) NOT NULL, "
			+ "`period` datetime NOT NULL, "
			+ "`sent_bytes` int(11) NOT NULL DEFAULT '0', "
			+ "`received_bytes` int(11) NOT NULL DEFAULT '0', "
			+ "`redirect_url` varchar(256) NOT NULL, "
			+ "`timestamp` bigint(20) NOT NULL DEFAULT '0', "
			+ "PRIMARY KEY (`id`)) ENGINE=InnoDB  DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1;";
	private final String GET_COUNT_QUERY = "SELECT COUNT(id) as count, COUNT(DISTINCT(src_ip)) "
			+ "as count_ip FROM statistic";
	private final String GET_DESTIN_QUERY = "SELECT src_ip, COUNT(id) "
			+ "AS count, MAX(`period`) "
			+ "AS `period` FROM statistic GROUP BY src_ip";
	private final String GET_REDIR_QUERY = "SELECT redirect_url, COUNT(id) AS "
			+ "count FROM statistic WHERE redirect_url != '' GROUP BY redirect_url";
	private final String GET_LAST_REQ_QUERY = "SELECT src_ip, uri, timestamp, sent_bytes, received_bytes "
			+ "FROM statistic ORDER BY `period` DESC LIMIT ";
	private final String GET_ALL_REQ_QUERY = "SELECT * FROM statistic";
	private final String FILL_DB = "INSERT INTO `statistic` "
			+ "(`src_ip`,`uri`, `period`, `redirect_url`, `received_bytes`, `sent_bytes`, `timestamp`) "
			+ "VALUES ('%s', '%s', '%s', '%s', %s, %s, %s)";

	private Statement createConnection() {
		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(dbUrl, user, pass);
			stmt = conn.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stmt;
	}

	public RequestJdbcDao() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("db.properties"));
			jdbcDriver = prop.getProperty("driver");
			dbUrl = prop.getProperty("db_url");
			user = prop.getProperty("user");
			pass = prop.getProperty("pass");
			try {
				createConnection().execute(CREATE_TABLE);
				stmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public StatusCommand getStatusCommand() {
		StatusCommand stc = new StatusCommand();
		try {
			createConnection();
			Map<String, Integer> countMap = this.getCount();
			stc.setTotalCount(countMap.get("totalCount"));
			stc.setIpCount(countMap.get("ipCount"));
			stc.setIpRequests(this.getIpRequests());
			stc.setRedirectUrls(this.getRedirectUrls());
			stc.setLastRequests(this.getLastRequests(16));
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return stc;

	}

	private Map<String, Integer> getCount() throws SQLException {
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		ResultSet rs = stmt.executeQuery(GET_COUNT_QUERY);
		rs.next();
		countMap.put("totalCount", rs.getInt("count"));
		countMap.put("ipCount", rs.getInt("count_ip"));
		rs.close();
		return countMap;
	}

	private List<Map<String, String>> getIpRequests() throws SQLException {
		List<Map<String, String>> ipRequests = new ArrayList<Map<String, String>>();
		ResultSet rs = stmt.executeQuery(GET_DESTIN_QUERY);
		while (rs.next()) {
			Map<String, String> row = new HashMap<String, String>();
			row.put("src_ip", rs.getString("src_ip"));
			row.put("count", rs.getString("count"));
			row.put("period", rs.getString("period"));
			ipRequests.add(row);
		}
		rs.close();
		return ipRequests;
	}

	private List<Map<String, String>> getRedirectUrls() throws SQLException {
		List<Map<String, String>> redirectUrls = new ArrayList<Map<String, String>>();
		ResultSet rs = stmt.executeQuery(GET_REDIR_QUERY);
		while (rs.next()) {
			Map<String, String> row = new HashMap<String, String>();
			row.put("redirect_url", rs.getString("redirect_url"));
			row.put("count", rs.getString("count"));
			redirectUrls.add(row);
		}
		rs.close();
		return redirectUrls;
	}

	private List<Request> getLastRequests(Integer count) throws SQLException {
		List<Request> requests = new ArrayList<Request>();
		ResultSet rs = stmt.executeQuery(GET_LAST_REQ_QUERY + count.toString());
		while (rs.next()) {
			Request request = new Request();
			request.setIp(rs.getString("src_ip"));
			request.setUri(rs.getString("uri"));
			request.setTimestamp(rs.getLong("timestamp"));
			request.setReceived_bytes(rs.getInt("received_bytes"));
			request.setSentBytes(rs.getInt("sent_bytes"));
			requests.add(request);
		}
		rs.close();
		return requests;
	}

	public void saveRequest(Request request) {
		try {
			String use = String.format(FILL_DB, request.getIp(),
					request.getUri(), this.format.format(request.getPeriod()),
					request.getRedirectUrl(), request.getReceived_bytes(),
					request.getSentBytes(), request.getTimestamp());
			createConnection().execute(use);
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

	public List<Request> getAllRequests() {

		List<Request> requests = new ArrayList<Request>();
		try {
			ResultSet rs = createConnection().executeQuery(GET_ALL_REQ_QUERY);
			while (rs.next()) {
				Request request = new Request();
				request.setId(rs.getInt("id"));
				request.setIp(rs.getString("src_ip"));
				request.setUri(rs.getString("uri"));
				request.setPeriod(this.format.parse(rs.getString("period")));
				request.setReceived_bytes(rs.getInt("received_bytes"));
				request.setSentBytes(rs.getInt("sent_bytes"));
				request.setRedirectUrl(rs.getString("redirect_url"));
				requests.add(request);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return requests;
	}
}

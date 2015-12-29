package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SqlHelper {

	PreparedStatement ps = null;
	Connection ct = null;
	ResultSet rs = null;
	String url = "jdbc:mysql://127.0.0.1:1234/weixin";
	String user = "root";
	String passwd = "root";

	public SqlHelper() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			ct = DriverManager.getConnection(url, user, passwd);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultSet query(String sql, String[] paras) {
		try {
			ps = ct.prepareStatement(sql);
			for (int i = 0; i < paras.length; i++) {
				ps.setString(i + 1, paras[i]);
			}
			rs = ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	public boolean execute(String sql, String[] paras) {
		boolean exe = true;
		try {
			ps = ct.prepareStatement(sql);
			for (int i = 0; i < paras.length; i++) {
				ps.setString(i + 1, paras[i]);
			}
			if (ps.executeUpdate() != 1) {
				exe = false;
			}
		} catch (Exception ex) {
			exe = false;
			ex.printStackTrace();
		} finally {
			close();
		}
		return exe;

	}

	public void close() {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (ct != null)
				ct.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

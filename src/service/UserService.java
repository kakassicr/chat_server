package service;
import java.sql.ResultSet;

import db.SqlHelper;

public class UserService {
	ResultSet rs=null;
	SqlHelper sqlHelper = null;
    String[] s={};
    String sql="";
 // 验证用户

	public boolean CheckUser(String account, String password) {
		boolean b = false;
		String passwd = ""; // 从数据库得到的密码
		try {
			sql = "select password from users where account=? limit 1";
			String[] paras = { account };
			sqlHelper = new SqlHelper();
			rs = sqlHelper.query(sql, paras);
			if (rs.next()) {
				passwd = rs.getString(1);
			} else {
				System.out.println("用户不存在");
				return b;
			}
			// 判断密码是否正确
			if (password.equals(passwd)) {
				System.out.println("验证成功");
				b = true;
			}else{
					System.out.println("密码错误");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			sqlHelper.close();
		}
		
		return b;
	}
	
	public String getFriendlist(String account){
    	String friendlist="";
    	try {
			sql = "select account from users where id in ("
					+ "select friendId from friendlist where userId=("
					+ "select id from users where account=? limit 1))";
			String[] paras = { account };	
			sqlHelper = new SqlHelper();
			rs = sqlHelper.query(sql, paras);
			while (rs.next()) {
				friendlist+=rs.getString(1)+" ";
            }
        if(rs!=null) rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	sqlHelper.close();
        }
    	System.out.println(friendlist);
            return friendlist;
        }
}

package service;
import java.sql.ResultSet;

import db.SqlHelper;

public class UserService {
	ResultSet rs=null;
	SqlHelper sqlHelper = null;
    String[] s={};
    String sql="";
 // ��֤�û�

	public boolean CheckUser(String account, String password) {
		boolean b = false;
		String passwd = ""; // �����ݿ�õ�������
		try {
			sql = "select password from users where account=? limit 1";
			String[] paras = { account };
			sqlHelper = new SqlHelper();
			rs = sqlHelper.query(sql, paras);
			if (rs.next()) {
				passwd = rs.getString(1);
			} else {
				System.out.println("�û�������");
				return b;
			}
			// �ж������Ƿ���ȷ
			if (password.equals(passwd)) {
				System.out.println("��֤�ɹ�");
				b = true;
			}else{
					System.out.println("�������");
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

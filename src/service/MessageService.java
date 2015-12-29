package service;

import java.util.ArrayList;
import java.sql.ResultSet;
import common.Message;
import db.SqlHelper;

public class MessageService {
	ResultSet rs=null;
	SqlHelper sqlHelper=null;
	String sql = "";
	public boolean addMessage(Message message){
		sql="insert into messages(sender,getter,content,sendTimer,isGet) values(?,?,?,?,?)";
		String[] paras = {message.getSender(),message.getGetter(),
				message.getCon(),message.getSendTime(),message.getIsGet()+""};
		return new SqlHelper().execute(sql, paras);
	}
	
	public ArrayList<Message> queryMessage(String getter){
		sql="select sender,getter,content,sendTimer from messages where isGet=1 and getter=?";
		String[] paras={getter};
		sqlHelper=new SqlHelper();
		rs=sqlHelper.query(sql, paras);
		ArrayList<Message> al=new ArrayList<Message>();
		try{
			while(rs.next()){
				Message msg=new Message();
				msg.setSender(rs.getString(1));
				msg.setGetter(getter);
				msg.setCon(rs.getString(3));
				msg.setSendTime(rs.getString(4));
				al.add(msg);
			}
			sqlHelper.close();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return al;
	}
	
	public boolean updateMessage(String getter){
		sql="update messages set isGet=0 where isGet=1 and getter=?";
		String[] paras={getter};
		sqlHelper=new SqlHelper();
		return new SqlHelper().execute(sql, paras);
	}
}

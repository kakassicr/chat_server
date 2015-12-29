package ctr;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.*;

import common.Config;
import common.Message;
import java.util.ArrayList;
public class ActionSer extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response) 
	throws ServletException, IOException {
		String action = request.getParameter("action");
		UserService userService = new UserService();
		if (action != null) {
			//登录ACTION
			if (action.equals(Config.ACTION_LOGIN)) {
				System.out.println("action为login");
				String account = request.getParameter("account");
				String password = request.getParameter("password");
				System.out.println("account:" + account);
				System.out.println("password:" + password);
				if (userService.CheckUser(account, password)) {
					String friendlist = userService.getFriendlist(account);
					PrintWriter pw = response.getWriter();
					pw.println(friendlist);
				} else {
					PrintWriter pw = response.getWriter();
					pw.println(Config.RESULT_STATUS_FAIL);
				}
			}
			//将客户端消息写入数据库ACTION
			else if (action.equals(Config.ACTION_SENDMESSAGE)) {
				System.out.println("action为sendMessage");	
				Message message=new Message();//创建消息对象
				message.setSender(request.getParameter(Config.KEY_SENDER));
				message.setGetter(request.getParameter(Config.KEY_GETTER));
				String content = request.getParameter(Config.KEY_CONTENT); //消息为中文时需要转码
				content = new String(content.getBytes("ISO-8859-1"), "utf-8"); 
				message.setCon(content);
				message.setSendTime(request.getParameter(Config.KEY_SENDTIME));
				message.setIsGet(Config.NOT_GET);
				System.out.println(message.getSender() + " 给 " + message.getGetter()
						+ " 说:" + message.getCon());			
				MessageService messageService=new MessageService();//将消息写入数据库
				boolean b=messageService.addMessage(message);
				if(b){
					PrintWriter pw = response.getWriter();
					pw.println(Config.RESULT_STATUS_SUCCESS);
				}else{
					PrintWriter pw = response.getWriter();
					pw.println(Config.RESULT_STATUS_FAIL);
				}
			}
			//将数据库消息返回给客户端
			else if(action.equals(Config.ACTION_GETMESSAGE)){
				System.out.println("action为getMessage");	
				String getter=request.getParameter(Config.KEY_ACCOUNT);//取出用户名
				MessageService messageService=new MessageService();
				ArrayList<Message> al=messageService.queryMessage(getter);//从数据库查询消息
				//将消息拼接成json数组
				if(!al.isEmpty()){
					StringBuffer messages=new StringBuffer();
					Message msg=new Message();
					messages.append("{\"status\":1,\"Messages\":[");
					for(int i=0;i<al.size()-1;i++){
						msg=al.get(i);
						messages.append("{\"content\":\"").append(msg.getCon())
						.append("\",\"sender\":\"").append(msg.getSender())
						.append("\",\"getter\":\"").append(getter)
						.append("\",\"sendtime\":\"").append(msg.getSendTime())
						.append("\"},");
					}
					//最后一项消息需要不需要加逗号，所以单独拼接
					msg=al.get(al.size()-1);
					messages.append("{\"content\":\"").append(msg.getCon())
					.append("\",\"sender\":\"").append(msg.getSender())
					.append("\",\"getter\":\"").append(getter)
					.append("\",\"sendtime\":\"").append(msg.getSendTime())
					.append("\"}]}");			
//					拼接json数组示例："{\"status\":1,\"Messages\":["+
//					"{\"content\":\"Hehe1\",\"sender\":\"a2\",\"getter\":\"a1\",\"sendtime\":\"2015-12-25 02:04:12\"},"+
//					"{\"content\":\"Hehe2\",\"sender\":\"a2\",\"getter\":\"a1\",\"sendtime\":\"2015-12-25 02:04:12\"},"+
//					"{\"content\":\"Hehe3\",\"sender\":\"a2\",\"getter\":\"a1\",\"sendtime\":\"2015-12-25 02:04:12\"}"+
//								"]}"
					System.out.println("Message:"+messages);	
					messageService.updateMessage(getter);//将数据库中消息的状态从未传给getter变成已传给getter
					response.setContentType("text/html;charset=UTF-8");//消息传给getter前需要转码
//效果同上：			response.setCharacterEncoding("UTF-8");
					PrintWriter pw = response.getWriter();
					pw.print(messages);
				}
		}
		}else {
			System.out.println("没有指定action");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}


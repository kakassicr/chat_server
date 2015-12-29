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
			//��¼ACTION
			if (action.equals(Config.ACTION_LOGIN)) {
				System.out.println("actionΪlogin");
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
			//���ͻ�����Ϣд�����ݿ�ACTION
			else if (action.equals(Config.ACTION_SENDMESSAGE)) {
				System.out.println("actionΪsendMessage");	
				Message message=new Message();//������Ϣ����
				message.setSender(request.getParameter(Config.KEY_SENDER));
				message.setGetter(request.getParameter(Config.KEY_GETTER));
				String content = request.getParameter(Config.KEY_CONTENT); //��ϢΪ����ʱ��Ҫת��
				content = new String(content.getBytes("ISO-8859-1"), "utf-8"); 
				message.setCon(content);
				message.setSendTime(request.getParameter(Config.KEY_SENDTIME));
				message.setIsGet(Config.NOT_GET);
				System.out.println(message.getSender() + " �� " + message.getGetter()
						+ " ˵:" + message.getCon());			
				MessageService messageService=new MessageService();//����Ϣд�����ݿ�
				boolean b=messageService.addMessage(message);
				if(b){
					PrintWriter pw = response.getWriter();
					pw.println(Config.RESULT_STATUS_SUCCESS);
				}else{
					PrintWriter pw = response.getWriter();
					pw.println(Config.RESULT_STATUS_FAIL);
				}
			}
			//�����ݿ���Ϣ���ظ��ͻ���
			else if(action.equals(Config.ACTION_GETMESSAGE)){
				System.out.println("actionΪgetMessage");	
				String getter=request.getParameter(Config.KEY_ACCOUNT);//ȡ���û���
				MessageService messageService=new MessageService();
				ArrayList<Message> al=messageService.queryMessage(getter);//�����ݿ��ѯ��Ϣ
				//����Ϣƴ�ӳ�json����
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
					//���һ����Ϣ��Ҫ����Ҫ�Ӷ��ţ����Ե���ƴ��
					msg=al.get(al.size()-1);
					messages.append("{\"content\":\"").append(msg.getCon())
					.append("\",\"sender\":\"").append(msg.getSender())
					.append("\",\"getter\":\"").append(getter)
					.append("\",\"sendtime\":\"").append(msg.getSendTime())
					.append("\"}]}");			
//					ƴ��json����ʾ����"{\"status\":1,\"Messages\":["+
//					"{\"content\":\"Hehe1\",\"sender\":\"a2\",\"getter\":\"a1\",\"sendtime\":\"2015-12-25 02:04:12\"},"+
//					"{\"content\":\"Hehe2\",\"sender\":\"a2\",\"getter\":\"a1\",\"sendtime\":\"2015-12-25 02:04:12\"},"+
//					"{\"content\":\"Hehe3\",\"sender\":\"a2\",\"getter\":\"a1\",\"sendtime\":\"2015-12-25 02:04:12\"}"+
//								"]}"
					System.out.println("Message:"+messages);	
					messageService.updateMessage(getter);//�����ݿ�����Ϣ��״̬��δ����getter����Ѵ���getter
					response.setContentType("text/html;charset=UTF-8");//��Ϣ����getterǰ��Ҫת��
//Ч��ͬ�ϣ�			response.setCharacterEncoding("UTF-8");
					PrintWriter pw = response.getWriter();
					pw.print(messages);
				}
		}
		}else {
			System.out.println("û��ָ��action");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}


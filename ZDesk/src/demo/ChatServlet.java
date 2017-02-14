package demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.CometEvent;
import org.apache.catalina.CometProcessor;

/**
 * 
 * @author Aries Zhao
 * 
 */
public class ChatServlet extends HttpServlet implements CometProcessor {

	private static final long serialVersionUID = -3667180332947986301L;

	// <用户,长连接>
	protected static Map<String, HttpServletResponse> connections = new HashMap<String, HttpServletResponse>();

	// 消息推送线程
	protected static MessageSender messageSender = null;

	public void init() throws ServletException {
		// 启动消息推送线程
		messageSender = new MessageSender();
		Thread messageSenderThread = new Thread(messageSender, "MessageSender["
				+ getServletContext().getContextPath() + "]");
		messageSenderThread.setDaemon(true);
		messageSenderThread.start();
	}

	public void destroy() {
		connections.clear();
		messageSender.stop();
		messageSender = null;
	}

	public void event(CometEvent event) throws IOException, ServletException {
		HttpServletRequest request = event.getHttpServletRequest();
		HttpServletResponse response = event.getHttpServletResponse();

		// 昵称
		String name = request.getParameter("name");
		if (name == null) {
			return;
		}

		if (event.getEventType() == CometEvent.EventType.BEGIN) {
			// Http连接空闲超时
			event.setTimeout(Integer.MAX_VALUE);
			log("Begin for session: " + request.getSession(true).getId());

			// 创建Comet Iframe
			PrintWriter writer = response.getWriter();
			writer.println("<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">");
			writer.println("<html><head><script type=\"text/javascript\">var comet = window.parent.comet;</script></head><body>");
			writer.println("<script type=\"text/javascript\">");
			writer.println("var comet = window.parent.comet;");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Timestamp tt=new Timestamp(System.currentTimeMillis());
			String ttStr=format.format(tt);
//			writer.println("var timestatmServ = "+System.currentTimeMillis()+";");
//			writer.println("var timestatmLocal = (new Date()).getTime();");
			writer.println("</script>");
			writer.flush();

			// for chrome
			if (request.getHeader("User-Agent").contains("KHTML")) {
				for (int i = 0; i < 100; i++) {
					writer.print("<input type=hidden name=none value=none>");
				}
				writer.flush();
			}

			// 欢迎信息
			writer.print("<script type=\"text/javascript\">");
			writer.println("comet.newMessage('Hello " + name + ", Welcome!');");
			writer.print("</script>");
			writer.flush();

			// 通知其他用户有新用户登陆
			if (!connections.containsKey(name)) {
				messageSender.login(name);
			}

			// 推送已经登陆的用户信息
			for (String user : connections.keySet()) {
				if (!user.equals(name)) {
					writer.print("<script type=\"text/javascript\">");
					writer.println("comet.newUser('" + user + "');");
					writer.print("</script>");
				}
			}
			writer.flush();

			synchronized (connections) {
				connections.put(name, response);
			}
		} else if (event.getEventType() == CometEvent.EventType.ERROR) {
			log("Error for session: " + request.getSession(true).getId());
			synchronized (connections) {
				connections.remove(name);
			}
			event.close();
		} else if (event.getEventType() == CometEvent.EventType.END) {
			log("End for session: " + request.getSession(true).getId());
			messageSender.logout(name);
			synchronized (connections) {
				connections.remove(name);
			}
			PrintWriter writer = response.getWriter();
			writer.println("</body></html>");
			event.close();
		} else if (event.getEventType() == CometEvent.EventType.READ) {
			InputStream is = request.getInputStream();
			byte[] buf = new byte[512];
			do {
				int n = is.read(buf); // can throw an IOException
				if (n > 0) {
					log("Read " + n + " bytes: " + new String(buf, 0, n)
							+ " for session: "
							+ request.getSession(true).getId());
				} else if (n < 0) {
					return;
				}
			} while (is.available() > 0);
		}
	}

	// 发送消息给所有人
	public static void send(String message) {
		messageSender.send("*", message);
	}

	// 向某个连接发送消息
	public static void send(String name, String message) {
		messageSender.send(name, message);
	}

	public class MessageSender implements Runnable {

		protected boolean running = true;
		protected Map<String, String> messages = new HashMap<String, String>();

		public MessageSender() {

		}

		public void stop() {
			running = false;
		}

		// 新用户登陆
		public void login(String name) {
			synchronized (messages) {
				messages.put("Login", name);
				messages.notify();
			}
		}

		// 用户下线
		public void logout(String name) {
			synchronized (messages) {
				messages.put("Logout", name);
				messages.notify();
			}
		}

		// 发送消息
		public void send(String user, String message) {
			synchronized (messages) {
				messages.put(user, message);
				messages.notify();
			}
		}

		public void run() {
			while (running) {
//				if (messages.size() == 0) {
//					try {
//						synchronized (messages) {
//							messages.wait();
//						}
//					} catch (InterruptedException e) {
//						// Ignore
//					}
//				}

				
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e) {
					log("InterruptedException sending message", e);
				}
				
				synchronized (connections) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Timestamp tt=new Timestamp(System.currentTimeMillis());
					String ttStr=format.format(tt);
					log("Send 1 message:  to everyone."+ttStr);
					for (HttpServletResponse response : connections.values()) {
						try {
							PrintWriter writer = response.getWriter();
							writer.print("<script type=\"text/javascript\" msg='<?xml version=\"1.0\" encoding=\"GBK\"?><datasets><table id=\"zuoxizhuangtaimingxijiankong\"><item AgentId=\"6014\" StatuId=\"3\" StatuName=\"就绪\" ContinuedTime=\"93\" TelephoneCode=\"\" DealTime=\"00:01:33\" TelCount=\"21\" phyName=\"张天宠组\" agentName=\"毕涛\"  /><item AgentId=\"5555\" StatuId=\"6\" StatuName=\"话务整理\" ContinuedTime=\"14779\" TelephoneCode=\"\" DealTime=\"04:06:19\" TelCount=\"0\" phyName=\"YY_CC\" agentName=\"测试\"  /><item AgentId=\"6049\" StatuId=\"6\" StatuName=\"话务整理\" ContinuedTime=\"624\" TelephoneCode=\"\" DealTime=\"00:10:24\" TelCount=\"0\" phyName=\"张天宠组\" agentName=\"张天宠\"  /><item AgentId=\"6050\" StatuId=\"4\" StatuName=\"双方通话\" ContinuedTime=\"183\" TelephoneCode=\"13034002333\" DealTime=\"00:03:03\" TelCount=\"5\" phyName=\">张天宠组\" agentName=\"张健061465\"  /><item AgentId=\"8026\" StatuId=\"3\" StatuName=\"就绪\" ContinuedTime=\"1491\" TelephoneCode=\"\" DealTime=\"00:24:51\" TelCount=\"3\" phyName=\"VIP-郭鹏组\" agentName=\"纪萌萌\"  /></table></datasets>' >");
							writer.println("comet.newMessage('"+ttStr+"cccccccccccccccccccccccc就绪就绪就绪就绪就绪就绪就绪就绪','"+System.currentTimeMillis()+"');");
							writer.print("</script>");
							writer.flush();
						} catch (IOException e) {
							log("IOExeption execute command", e);
						}
					}
					
					
					synchronized (messages) {
						// 推送消息队列中的消息
						for (Entry<String, String> message : messages.entrySet()) {
							if (message.getKey().equals("Login")) {// 新用户登陆
								log(message.getValue() + " Login");
								for (HttpServletResponse response : connections.values()) {
									try {
										PrintWriter writer = response.getWriter();
										writer.print("<script type=\"text/javascript\">");
										writer.println("comet.newMessage('Welcome "
														+ message.getValue()
														+ " !');");
										writer.println("comet.newUser('"
												+ message.getValue() + "');");
										writer.print("</script>");
										writer.flush();
									} catch (IOException e) {
										log("IOExeption execute command", e);
									}
								}
							} else if ("Logout".equals(message.getKey())) {// 用户退出
								log(message.getValue() + " Logout");
								for (HttpServletResponse response : connections
										.values()) {
									try {
										PrintWriter writer = response.getWriter();
										writer.print("<script type=\"text/javascript\">");
										writer.println("comet.newMessage('88, "+ message.getValue() + "');");
										writer.println("comet.deleteUser('"+ message.getValue() + "');");
										writer.print("</script>");
										writer.flush();
									} catch (IOException e) {
										log("IOExeption execute command", e);
									}
								}
							} else if ("*".equals(message.getKey())) {// 群发消息
								log("Send message: " + message.getValue()
										+ " to everyone.");
								for (HttpServletResponse response : connections
										.values()) {
									try {
										PrintWriter writer = response
												.getWriter();
										writer
												.print("<script type=\"text/javascript\">");
										writer.println("comet.newMessage('"
												+ message.getValue() + "');");
										writer.print("</script>");
										writer.flush();
									} catch (IOException e) {
										log("IOExeption execute command", e);
									}
								}
							} else {// 向某人发信息
								try {
									HttpServletResponse response = connections
											.get(message.getKey());
									PrintWriter writer = response.getWriter();
									writer
											.print("<script type=\"text/javascript\">");
									writer.println("comet.privateMessage('"
											+ message.getValue() + "');");
									writer.print("</script>");
									writer.flush();
								} catch (IOException e) {
									log("IOExeption sending message", e);
								}
							}
							// 从消息队列中删除消息
							messages.remove(message.getKey());
						}
					}
				}
			}
		}
	}
}
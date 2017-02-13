package com.zinglabs.mq;

import java.io.BufferedReader;

import java.io.FileReader;

import com.ibm.mq.MQException;

public class MQSend extends MQConnector

{

	public MQSend()

	{

	}

	
	public void send2(String qMsg2) throws MQException

	{

//		boolean argsAreFiles = hasArg("-f", args);

		initMq("send");

		openQueue();
		putMessageToQueue(qMsg2);

//		for (int i = 0; i < args.length; i++)
//
//		{
//
//			if (args[i].equals("-f"))
//
//				continue;
//
//			if (!argsAreFiles)
//
//			{
//
//				
//
//			}
//
//			else
//
//			{
//
//				try
//
//				{
//
//					// send file contents as message 
//
//					BufferedReader br = new BufferedReader(new FileReader(
//							args[i]));
//
//					StringBuffer msg = new StringBuffer();
//
//					for (String line = br.readLine(); line != null; line = br
//							.readLine())
//
//					{
//
//						msg.append(line);
//
//						msg.append('\n');
//
//					}
//
//					br.close();
//
//					putMessageToQueue(msg.toString());
//
//				}
//
//				catch (Exception e)
//
//				{
//
//					System.out.println("Error while processing file " + args[i]
//							+ ": "
//
//							+ e.toString());
//
//				}
//
//			}
//
//			System.out.println("Message sent.");
//
//		}

		closeQueue();

		disconnectMq();

	}
	
	public void send(String[] args) throws MQException

	{

		boolean argsAreFiles = hasArg("-f", args);

		initMq("send");

		openQueue();

		for (int i = 0; i < args.length; i++)

		{

			if (args[i].equals("-f"))

				continue;

			if (!argsAreFiles)

			{

				putMessageToQueue(args[i]);

			}

			else

			{

				try

				{

					// send file contents as message 

					BufferedReader br = new BufferedReader(new FileReader(
							args[i]));

					StringBuffer msg = new StringBuffer();

					for (String line = br.readLine(); line != null; line = br
							.readLine())

					{

						msg.append(line);

						msg.append('\n');

					}

					br.close();

					putMessageToQueue(msg.toString());

				}

				catch (Exception e)

				{

					System.out.println("Error while processing file " + args[i]
							+ ": "

							+ e.toString());

				}

			}

			System.out.println("Message sent.");

		}

		closeQueue();

		disconnectMq();

	}

	
	public static void main(String[] args)

	{

		MQSend mqsend = new MQSend();

		MQConnector.DEBUG = true;

//		try
//
//		{
//
//			if (args == null || args.length == 0)
//
//			{
//
//				System.out.println("Usage: " + mqsend.getClass().getName()
//
//				+ " [-f] <file name | message> [<file name | message> ...]");
//
//				System.exit(0);
//
//			}
//
			try {
				mqsend.send2("33333333333333333333333333中文");
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//
//		}
//
//		catch (Exception e)
//
//		{
//
//			System.out.println(e.toString());
//
//			System.out.println("Usage: " + mqsend.getClass().getName()
//
//			+ " [-f] <file name | message> [<file name | message> ...]");
//
//		}

	}

}

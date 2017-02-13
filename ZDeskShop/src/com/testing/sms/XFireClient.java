package com.testing.sms;

import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

public class XFireClient{
//	private static final Log log = LogFactory.getLog(XFireClient.class);
	private static ObjectServiceFactory objectServiceFactory = new ObjectServiceFactory();
	private static XFireProxyFactory xfireProxyFactory = new XFireProxyFactory();

	public static Object getClient(String interfaceName, String url) {
		Service serviceModel = null;
		try {
			serviceModel = objectServiceFactory.create(Class
					.forName(interfaceName));
			//serviceModel.addOutHandler(new ClientAuthenticationHandler());
			//serviceModel.addInHandler(new ClientAuthenticationHandler());
		} catch (ClassNotFoundException e) {
	//		log.error(e.getMessage(), e);
			return null;
		}
		String serviceURL = url;
		Object service = null;
		try {
			service = xfireProxyFactory.create(serviceModel, serviceURL);
		
		} catch (Exception e) {
	//		log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return service;
	}
}

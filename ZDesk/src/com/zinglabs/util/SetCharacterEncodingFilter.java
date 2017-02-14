package com.zinglabs.util;

import javax.servlet.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.log.LogUtil;
import com.zinglabs.servlet.ZIMCommServlet;

import java.io.*;

public class SetCharacterEncodingFilter implements Filter {

	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	/**
	 * The default character encoding to set for requests that pass through this
	 * filter.
	 */
	protected String encoding = null;

	/**
	 * The filter configuration object we are associated with. If this value is
	 * null, this filter instance is not currently configured.
	 */
	protected FilterConfig filterConfig = null;

	/**
	 * Should a character encoding specified by the client be ignored?
	 */
	protected boolean ignore = true;

	// --------------------------------------------------------- Public Methods
	/**
	 * Take this filter out of service.
	 */
	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	/**
	 * Select and set (if specified) the character encoding to be used to
	 * interpret request parameters for this request.
	 *
	 * @param request
	 *            The servlet request we are processing
	 * @param result
	 *            The servlet response we are creating
	 * @param chain
	 *            The filter chain we are processing
	 *
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet error occurs
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// Conditionally select and set the character encoding to be used
		try {
			if (ignore || (request.getCharacterEncoding() == null)) {
				String encoding = selectEncoding(request);
				if (encoding != null)
					request.setCharacterEncoding(encoding);
			}
			chain.doFilter(request, response);
		} catch (Exception e) {
			LogUtil.error(e, SKIP_Logger);
			LogUtil.error(e.getCause().getMessage(), SKIP_Logger);
		}
	}

	/**
	 * Place this filter into service.
	 *
	 * @param filterConfig
	 *            The filter configuration object
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		encoding = filterConfig.getInitParameter("encoding");
		String value = filterConfig.getInitParameter("ignore");
		if (value == null)
			ignore = true;
		else if (value.equalsIgnoreCase("true"))
			ignore = true;
		else if (value.equalsIgnoreCase("yes"))
			ignore = true;
		else
			ignore = false;
	}

	// ------------------------------------------------------ Protected Methods
	/**
	 * Select an appropriate character encoding to be used, based on the
	 * characteristics of the current request and/or filter initialization
	 * parameters. If no character encoding should be set, return
	 * <code>null</code>.
	 * <p>
	 * The default implementation unconditionally returns the value configured
	 * by the <strong>encoding</strong> initialization parameter for this
	 * filter.
	 *
	 * @param request
	 *            The servlet request we are processing
	 */
	protected String selectEncoding(ServletRequest request) {
		return (this.encoding);
	}
}
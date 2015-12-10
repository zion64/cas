package org.jasig.cas.config;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import lombok.Data;

@Data
public class ServletSpec {
	private ServletContext servletContext;
	private String servletName;
	private Servlet servlet;
	private int loadOrder;
	private String[] mappingUrl;
	
	
	
	public ServletSpec(ServletContext servletContext, String servletName, Servlet servlet, int loadOrder,
			String[] mappingUrl) {
		super();
		this.servletContext = servletContext;
		this.servletName = servletName;
		this.servlet = servlet;
		this.loadOrder = loadOrder;
		this.mappingUrl = mappingUrl;
	}



	public ServletSpec() {
	}
	
}

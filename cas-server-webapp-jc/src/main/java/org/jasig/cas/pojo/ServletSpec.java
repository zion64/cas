package org.jasig.cas.pojo;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import lombok.Data;

import java.util.Arrays;

public class ServletSpec {
	private ServletContext servletContext;
	private String servletName;
	private Servlet servlet;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ServletSpec that = (ServletSpec) o;

		if (loadOrder != that.loadOrder) return false;
		if (!servletContext.equals(that.servletContext)) return false;
		if (!servletName.equals(that.servletName)) return false;
		if (!servlet.equals(that.servlet)) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		return Arrays.equals(mappingUrl, that.mappingUrl);

	}

	@Override
	public int hashCode() {
		int result = servletContext.hashCode();
		result = 31 * result + servletName.hashCode();
		result = 31 * result + servlet.hashCode();
		result = 31 * result + loadOrder;
		result = 31 * result + Arrays.hashCode(mappingUrl);
		return result;
	}

	private int loadOrder;
	private String[] mappingUrl;

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public String getServletName() {
		return servletName;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	public Servlet getServlet() {
		return servlet;
	}

	public void setServlet(Servlet servlet) {
		this.servlet = servlet;
	}

	public int getLoadOrder() {
		return loadOrder;
	}

	public void setLoadOrder(int loadOrder) {
		this.loadOrder = loadOrder;
	}

	public String[] getMappingUrl() {
		return mappingUrl;
	}

	public void setMappingUrl(String[] mappingUrl) {
		this.mappingUrl = mappingUrl;
	}

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

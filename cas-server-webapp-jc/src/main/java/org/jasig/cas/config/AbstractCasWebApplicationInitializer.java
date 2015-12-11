/**
 * 
 */
package org.jasig.cas.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import com.codahale.metrics.servlets.PingServlet;
import com.codahale.metrics.servlets.ThreadDumpServlet;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zion64
 *
 */
@Slf4j
public abstract class AbstractCasWebApplicationInitializer
		extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Getter @Setter
	private static String CAS_SERVLET_NAME = "cas-servlet";
	
	private static String CAS_HOME = "/opt/cas";
	
	/**
	 * @param servletContext
	 * @throws ServletException
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		
		this.initCasEnvironment();

		super.onStartup(servletContext);
		
		this.addAdditionalServlet(servletContext);

		
	}

	/**
	 * @throws ServletException
	 */
	private void initCasEnvironment() throws ServletException {
		try {
			this.initCasHomeDir();
			this.copyResourcesToCasHome();
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new ServletException(new IOException(e.getMessage()));
		}
	}

	/**
	 * @return
	 */
	@Override
	protected WebApplicationContext createRootApplicationContext() {
		Class<?>[] configClasses = getRootConfigClasses();
		if (!ObjectUtils.isEmpty(configClasses)) {
			AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();

			String[] springProfiles = this.getSpringProfiles();
			if (!ObjectUtils.isEmpty(springProfiles))
				rootAppContext.getEnvironment().setActiveProfiles(springProfiles);

			String displayName = this.getDisplayName();
			if (!ObjectUtils.isEmpty(displayName))
				rootAppContext.setDisplayName(displayName);

			rootAppContext.register(configClasses);
			return rootAppContext;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.support.
	 * AbstractAnnotationConfigDispatcherServletInitializer#getRootConfigClasses
	 * ()
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { RootConfig.class };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.support.
	 * AbstractAnnotationConfigDispatcherServletInitializer#
	 * getServletConfigClasses()
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { ServletConfig.class };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.support.
	 * AbstractDispatcherServletInitializer#getServletMappings()
	 */
	@Override
	protected String[] getServletMappings() {
		String[] basicCASServletMappings = new String[] { "/login", "/logout", "/validate", "/serviceValidate",
				"/p3/serviceValidate", "/proxy", "/proxyValidate", "/p3/proxyValidate", "/CentralAuthenticationService",
				"/status", "/statistics", "/statistics/ssosessions", "/status/config", "/authorizationFailure.html",
				"/v1/*" };

		String[] additionalMappings = this.getAdditionalServletMappingsForDispatcher();

		String[] mappings = ArrayUtils.isEmpty(additionalMappings) ? basicCASServletMappings
				: ArrayUtils.addAll(basicCASServletMappings, additionalMappings);

		return mappings;
	}

	/**
	 * @return
	 */
	protected abstract String[] getAdditionalServletMappingsForDispatcher();

	/**
	 * @return
	 */
	protected abstract List<ServletSpec> getAdditionalServlet();

	/**
	 * @return
	 */
	protected abstract String[] getSpringProfiles();

	/**
	 * @return
	 */
	protected abstract String getDisplayName();
	
	/**
	 * @return
	 */
	protected abstract String getCasHome();

	/**
	 * @return
	 */
	@Override
	protected String getServletName() {
		return CAS_SERVLET_NAME;
	}

	/**
	 * @param servletContext
	 */
	protected void addAdditionalServlet(ServletContext servletContext) {
		this.registAdditionalServlet(new ServletSpec(servletContext, "metrics-health", new HealthCheckServlet(), 2,
				new String[] { "/statistics/healthcheck" }));
		this.registAdditionalServlet(new ServletSpec(servletContext, "metrics", new MetricsServlet(), 3,
				new String[] { "/statistics/metrics" }));
		this.registAdditionalServlet(new ServletSpec(servletContext, "metrics-ping", new PingServlet(), 4,
				new String[] { "/statistics/ping" }));
		this.registAdditionalServlet(new ServletSpec(servletContext, "metrics-threads", new ThreadDumpServlet(), 5,
				new String[] { "/statistics/threads" }));

		for (ServletSpec spec : this.getAdditionalServlet()) {
			this.registAdditionalServlet(spec);
		}
	}

	/**
	 * @param spec
	 * @return
	 */
	private ServletRegistration.Dynamic registAdditionalServlet(ServletSpec spec) {
		ServletRegistration.Dynamic servlet = spec.getServletContext().addServlet(spec.getServletName(),
				spec.getServlet());
		servlet.setLoadOnStartup(spec.getLoadOrder());

		Set<String> mappingConflicts = servlet.addMapping(spec.getMappingUrl());
		this.checkServletUrlMappingConflict(mappingConflicts, spec.getServletName());
		return servlet;
	}

	/**
	 * @param mappingConflicts
	 * @param servletName
	 */
	private void checkServletUrlMappingConflict(Set<String> mappingConflicts, String servletName) {
		if (!mappingConflicts.isEmpty()) {
			for (String s : mappingConflicts) {
				log.error("{} {}", StringUtils.repeat('=', 5),
						"Mapping conflict : " + s + " for servlet:" + servletName);
				throw new IllegalStateException("Servlet cannot be mapped because url is conflicted with another ....");
			}
		}
	}
	
	
	/**
	 * If CAS Home Directory not exist, make directory, sub-directory to be necessary and copy resources.
	 * 
	 * Directory Structure
	 *   CAS_HOME/logs : log directory
	 *   CAS_HOME/i18n : message sources directory
	 *   CAS_HOME/conf : cas configuration directory
	 * 
	 * @throws IOException 
	 */
	private void initCasHomeDir() throws IOException {

		String casHome = this.getCasHome(); 
		if(!ObjectUtils.isEmpty(casHome)) CAS_HOME = casHome;

		FileSystemResource resource = new FileSystemResource(CAS_HOME);
		
		if(resource.exists()) {
			if(resource.getFile().isFile()) {
				log.error("CAS_HOME is not a directory. The current CAS_HOME in this configuration is {}", CAS_HOME);
				throw new FileAlreadyExistsException(CAS_HOME);
			} else {
				FileUtils.forceMkdir(new File(CAS_HOME + "/logs"));
				FileUtils.forceMkdir(new File(CAS_HOME + "/conf"));
				FileUtils.forceMkdir(new File(CAS_HOME + "/i18n"));
				FileUtils.forceMkdir(new File(CAS_HOME + "/services"));
			}
		} else {
			log.error("CAS_HOME is not exist. Current CAS_HOME is {}", CAS_HOME);
			throw new FileNotFoundException("CAS_HOME is not exist. Current CAS_HOME is " + CAS_HOME);
		}
	}
	
	private void copyResourcesToCasHome() {
		
	}
}

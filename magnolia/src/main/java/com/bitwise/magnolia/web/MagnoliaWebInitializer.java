package com.bitwise.magnolia.web;
/**
 *  
 * @author Sika Kay
 * @date 14/02/17
 *
 */
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import com.bitwise.magnolia.config.InfrastructureContextConfig;
import com.bitwise.magnolia.web.config.CORSFilterConfig;
import com.bitwise.magnolia.web.config.RestfulContextConfig;
import com.bitwise.magnolia.web.config.WebMvcContextConfig;

public class MagnoliaWebInitializer implements WebApplicationInitializer {

	private static final Class<?>[] basicConfigurationClasses = new Class<?>[] {
		WebMvcContextConfig.class, InfrastructureContextConfig.class,
		RestfulContextConfig.class};

	private static final String DISPATCHER_SERVLET_NAME = "dispatcher";
	
	private static final String RESTFUL_DISPATCHER_SERVLET_NAME = "restful";
	
	private static final long MAX_FILE_SIZE = 5242880;
	
	private static final long MAX_REQUEST_SIZE = 20971520;
	
	private static final int FILE_SIZE_THRESHOLD = 0;
	
	//private static final String LOCATION = "/tmp";
	
	private static final String LOCATION = "C://tmp";
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		registerListener(servletContext);
		registerDispatcherServlet(servletContext);
		registerCORSFilter(servletContext);
		registerHiddenHttpMethodFilter(servletContext);
		registerCharacterEncodingFilter(servletContext);
		registerRestfulDispatcherServlet(servletContext);
	}
	
	private void registerDispatcherServlet(ServletContext servletContext) {
		AnnotationConfigWebApplicationContext dispatcherContext = createContext(WebMvcContextConfig.class);
		ServletRegistration.Dynamic dispatcher;
		MultipartConfigElement multipartConfig = new MultipartConfigElement(LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE,
				FILE_SIZE_THRESHOLD);
		dispatcher = servletContext.addServlet(DISPATCHER_SERVLET_NAME, new DispatcherServlet(dispatcherContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.setMultipartConfig(multipartConfig);
		dispatcher.addMapping("/");
	}
	
	private void registerRestfulDispatcherServlet(ServletContext servletContext) {
		AnnotationConfigWebApplicationContext restfulDispatcherContext = createContext(RestfulContextConfig.class);
		ServletRegistration.Dynamic restfulDispatcher;
		restfulDispatcher = servletContext.addServlet(RESTFUL_DISPATCHER_SERVLET_NAME,
				new DispatcherServlet(restfulDispatcherContext));
		restfulDispatcher.setLoadOnStartup(1);
		restfulDispatcher.addMapping("/restful/**");
	}
	
	private void registerListener(ServletContext servletContext) {
		AnnotationConfigWebApplicationContext rootContext = createContext(basicConfigurationClasses);
		servletContext.addListener(new ContextLoaderListener(rootContext));
		servletContext.addListener(new RequestContextListener());
	}
	
	private void registerHiddenHttpMethodFilter(ServletContext servletContext) {
		FilterRegistration.Dynamic registration;
		registration = servletContext.addFilter("hiddenHttpMethodFilter", HiddenHttpMethodFilter.class);
		registration.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false,
				DISPATCHER_SERVLET_NAME);
	}
	
	private void registerCORSFilter(ServletContext servletContext) {
		FilterRegistration.Dynamic registration;
		registration = servletContext.addFilter("corsFilter", CORSFilterConfig.class);
		registration.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false,
				DISPATCHER_SERVLET_NAME);
	}
	
	private void registerCharacterEncodingFilter(ServletContext servletContext) {
		FilterRegistration.Dynamic registration;
		registration = servletContext.addFilter("characterEncodingFilter", CharacterEncodingFilter.class);
		registration.setInitParameter("encoding", "utf-8");
		registration.setInitParameter("forceEncoding", "true");
		registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, "/*");
	}
	
	private AnnotationConfigWebApplicationContext createContext(final Class<?>... annotatedClasses) {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(annotatedClasses);
		return context;
	}
}

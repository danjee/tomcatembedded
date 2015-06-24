package ro.danjee.tomcat;

import java.io.File;

import javax.servlet.Filter;

import org.apache.catalina.Context;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

/**
 * Created by djipa on 6/3/15 10:01 AM.
 */
public class Main {

	public static void main(String[] args) throws Exception {

		Tomcat tomcat = new Tomcat();
		Service service = tomcat.getService();
		service.addConnector(getSslConnector());
		
		
		File base = new File(System.getProperty("java.io.tmpdir"));
		Context rootCtx = tomcat.addContext("/", base.getAbsolutePath());
		rootCtx.addFilterDef(createFilterDef("pippoFilter", new EmptyFilter()));
		rootCtx.addFilterMap(createFilterMap("pippoFilter", "/*"));
		Tomcat.addServlet(rootCtx, "emptyServlet", new EmptyServlet());
		rootCtx.addServletMapping("/*", "emptyServlet");
		tomcat.start();
		tomcat.getServer().await();

	}

	private static Connector getSslConnector() {
		Connector connector = new Connector();
		connector.setPort(9000);
		connector.setSecure(true);
		connector.setScheme("https");
		connector.setAttribute("keyAlias", "tomcat");
		connector.setAttribute("keystorePass", "password");
		connector.setAttribute("keystoreType", "JKS");
		connector.setAttribute("keystoreFile",
				"keystore.jks");
		connector.setAttribute("clientAuth", "false");
		connector.setAttribute("protocol", "HTTP/1.1");
		connector.setAttribute("sslProtocol", "TLS");
		connector.setAttribute("maxThreads", "200");
		connector.setAttribute("protocol", "org.apache.coyote.http11.Http11AprProtocol");
		connector.setAttribute("SSLEnabled", true);
		return connector;
	}

	private static FilterDef createFilterDef(String filterName, Filter filter) {
		FilterDef filterDef = new FilterDef();
		filterDef.setFilterName(filterName);
		filterDef.setFilter(filter);
		return filterDef;
	}

	private static FilterMap createFilterMap(String filterName,
			String urlPattern) {
		FilterMap filterMap = new FilterMap();
		filterMap.setFilterName(filterName);
		filterMap.addURLPattern(urlPattern);
		return filterMap;
	}
}

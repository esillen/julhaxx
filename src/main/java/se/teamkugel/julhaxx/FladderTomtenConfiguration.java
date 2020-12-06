package se.teamkugel.julhaxx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.RuntimeErrorException;

import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

// Based on tutorial https://www.vojtechruzicka.com/spring-boot-add-war-to-embedded-tomcat/
/*
@Configuration
public class FladderTomtenConfiguration {
	@Bean
	@ConditionalOnProperty(name="fladdertomten.war.file")
	public TomcatServletWebServerFactory servletContainerFactory(
			@Value("${fladdertomten.war.file}") String path,
			@Value("${fladdertomten.war.context}") String contextPath) {
		return new TomcatServletWebServerFactory() {

			@Override
			protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
				new File(tomcat.getServer().getCatalinaBase(), "webapps").mkdirs();
				
				try {
					tomcat.addWebapp(contextPath, ResourceUtils.getFile(path).toURI().toURL());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				return super.getTomcatWebServer(tomcat);
			}

		};
	}
}
*/


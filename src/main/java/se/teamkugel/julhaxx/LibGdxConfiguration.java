package se.teamkugel.julhaxx;

import java.io.File;

import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

// Based on tutorial https://www.vojtechruzicka.com/spring-boot-add-war-to-embedded-tomcat/

@Configuration
public class LibGdxConfiguration {
	@Bean
	public TomcatServletWebServerFactory servletContainerFactory(
			@Value("${fladdertomten.war.file}") String fladderTomtenPath,
			@Value("${fladdertomten.war.context}") String fladderTomtenContextPath,
			@Value("${santachemistry.war.file}") String santaChemistryPath,
			@Value("${santachemistry.war.context}") String santaChemistryContextPath) {
		return new TomcatServletWebServerFactory() {

			@Override
			protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
				new File(tomcat.getServer().getCatalinaBase(), "webapps").mkdirs();
				
				addLibGdxWebapp(tomcat, fladderTomtenContextPath, fladderTomtenPath);
				addLibGdxWebapp(tomcat, santaChemistryContextPath, santaChemistryPath);

				return super.getTomcatWebServer(tomcat);
			}
		};
	}
	
	private void addLibGdxWebapp(Tomcat tomcat, String context, String warFile) {
		try {
			tomcat.addWebapp(context, ResourceUtils.getFile(warFile).toURI().toURL());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}


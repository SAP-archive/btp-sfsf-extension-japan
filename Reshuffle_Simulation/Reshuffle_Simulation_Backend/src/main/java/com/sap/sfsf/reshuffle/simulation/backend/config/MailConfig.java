package com.sap.sfsf.reshuffle.simulation.backend.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;


@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {
	private Logger LOG = LoggerFactory.getLogger(MailConfig.class);

	@Value("${mail.protocol}")
	private String protocol;
	@Value("${mail.smtp.auth}")
	private boolean auth;
	@Value("${mail.smtp.starttls.enable}")
	private boolean starttls;
	
	private String host;
	private int port;
	private String username;
	private String password;

	@Bean
	public JavaMailSender javaMailSender() {
		Destination mailDest = DestinationAccessor.getDestination("MAIL");
		for(String prop:mailDest.getPropertyNames()) {
			LOG.debug(prop);
		}
		host = mailDest.get("mail.smtp.host").getOrElse("").toString();
		port = Integer.parseInt(mailDest.get("mail.smtp.port").getOrElse("587").toString());
		username = mailDest.get("mail.user").getOrElse("").toString();
		password = mailDest.get("mail.password").getOrElse("").toString();
		
		LOG.debug("Host:"+host);
		LOG.debug("Port:"+port);
		LOG.debug("Username:"+username);
		//LOG.debug("Password:"+password);
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		Properties mailProperties = new Properties();
		mailProperties.put("mail.smtp.auth", auth);
		mailProperties.put("mail.smtp.starttls.enable", starttls);
		mailSender.setJavaMailProperties(mailProperties);
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setProtocol(protocol);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		return mailSender;
	}
}

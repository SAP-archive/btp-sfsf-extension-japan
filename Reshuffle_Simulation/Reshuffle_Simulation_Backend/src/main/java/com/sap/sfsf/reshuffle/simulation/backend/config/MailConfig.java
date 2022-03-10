package com.sap.sfsf.reshuffle.simulation.backend.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;


@Configuration
public class MailConfig {
	private Logger LOG = LoggerFactory.getLogger(MailConfig.class);

	private String protocol;
	private boolean auth;
	private boolean starttls;
	private String trust;
	private String protocols;
	private boolean checkserveridentity;    	
    private String username;
    private String password;
    private String host;
	private int port;

	@Bean
	public JavaMailSender javaMailSender() {
		Destination mailDest = DestinationAccessor.getDestination("MAIL");
		for(String prop:mailDest.getPropertyNames()) {
			LOG.debug(prop);
		}
        
        if("true".equals(mailDest.get("mail.smtp.auth").getOrElse("").toString())){
            auth = true;
        }

        if("true".equals(mailDest.get("mail.smtp.starttls.enable").getOrElse("").toString())){
            starttls = true;
        }

        if("true".equals(mailDest.get("mail.smtp.ssl.checkserveridentity").getOrElse("").toString())){
            checkserveridentity = true;
        }
        protocols = mailDest.get("mail.smtp.ssl.protocols").getOrElse("").toString();
        trust = mailDest.get("mail.smtp.ssl.trust").getOrElse("").toString();

        host = mailDest.get("mail.smtp.host").getOrElse("").toString();
		port = Integer.parseInt(mailDest.get("mail.smtp.port").getOrElse("587").toString());
        protocol=mailDest.get("mail.protocol").getOrElse("").toString();
        username = mailDest.get("mail.smtp.user").getOrElse("").toString();;
        password = mailDest.get("mail.smtp.password").getOrElse("").toString();;
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		Properties mailProperties = new Properties();
		mailProperties.put("mail.smtp.auth", auth);
		mailProperties.put("mail.smtp.starttls.enable", starttls);
        mailProperties.put("mail.smtp.ssl.trust", trust);
        mailProperties.put("mail.smtp.ssl.protocols", protocols);
        mailProperties.put("mail.smtp.ssl.checkserveridentity", checkserveridentity);
		mailSender.setJavaMailProperties(mailProperties);
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setProtocol(protocol);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		return mailSender;
	}
}

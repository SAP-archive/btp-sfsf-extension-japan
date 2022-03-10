package com.sap.sfsf.reshuffle.applicants.backend.config;

import static org.springframework.http.HttpMethod.POST;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import com.sap.cloud.security.xsuaa.token.TokenAuthenticationConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
@EnableWebSecurity
@Order(1) // needs to have higher priority than CAP security config
// @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true,
// jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	XsuaaServiceConfiguration xsuaaServiceConfiguration;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	// @formatter:off
		http
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session is created by approuter
			.and()
				.csrf().disable()
				.authorizeRequests()
				.antMatchers(POST,"/odata/v4/ReshuffleService").permitAll()
				.antMatchers(POST,"/api/v4/ReshuffleService/exportFile").permitAll()
			.and()
				.oauth2ResourceServer()
				.jwt()
				.jwtAuthenticationConverter(getJwtAuthenticationConverter());
		// @formatter:on
	}

	// Customizes how GrantedAuthority are derived from a Jwt
	Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter() {
		TokenAuthenticationConverter converter = new TokenAuthenticationConverter(xsuaaServiceConfiguration);
		converter.setLocalScopeAsAuthorities(true);
		return converter;
	}

}
/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.security.oauth2;

import java.util.List;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Dave Syer
 *
 */
@ConfigurationProperties("oauth2.client")
@Data
public class OAuth2ClientProperties implements Validator {

	@Value("${vcap.services.${oauth2.sso.serviceId:sso}.credentials.tokenUri:${vcap.services.${oauth2.resource.serviceId:resource}.credentials.tokenUri:}}")
	private String tokenUri;

	@Value("${vcap.services.${oauth2.sso.serviceId:sso}.credentials.authorizationUri:${vcap.services.${oauth2.resource.serviceId:resource}.credentials.authorizationUri:}}")
	private String authorizationUri;

	@Value("${vcap.services.${oauth2.sso.serviceId:sso}.credentials.clientId:${vcap.services.${oauth2.resource.serviceId:resource}.credentials.clientId:}}")
	private String clientId;

	@Value("${vcap.services.${oauth2.sso.serviceId:sso}.credentials.clientSecret:${vcap.services.${oauth2.resource.serviceId:resource}.credentials.clientSecret:}}")
	private String clientSecret;

	@Value("${vcap.services.${oauth2.sso.serviceId:sso}.credentials.scope:${vcap.services.${oauth2.resource.serviceId:resource}.credentials.scope:}}")
	private List<String> scope;

	private AuthenticationScheme authenticationScheme = AuthenticationScheme.header;

	@Override
	public boolean supports(Class<?> clazz) {
		return OAuth2ClientProperties.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		OAuth2ClientProperties sso = (OAuth2ClientProperties) target;
		if (StringUtils.hasText(sso.getClientId())) {
			if (!StringUtils.hasText(sso.getAuthorizationUri())) {
				errors.rejectValue("authorizeUri", "missing.authorizeUri",
						"Missing authorizeUri");
			}
			if (!StringUtils.hasText(sso.getTokenUri())) {
				errors.rejectValue("tokenUri", "missing.tokenUri", "Missing tokenUri");
			}
			if (!StringUtils.hasText(sso.getClientSecret())) {
				errors.rejectValue("clientSecret", "missing.clientSecret",
						"Missing clientSecret");
			}
		}
	}

}

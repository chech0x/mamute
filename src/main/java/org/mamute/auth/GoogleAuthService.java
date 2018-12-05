package org.mamute.auth;

import javax.inject.Inject;

import org.mamute.qualifiers.Google;

import com.github.scribejava.core.oauth.OAuth20Service;

/*
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;*/

public class GoogleAuthService {
	/*private static final OAuth2AccessToken */
	@Inject @Google private OAuth20Service service;
	
	public String getOauthUrl(String redirect) {
		String url = service.getAuthorizationUrl();
		if (redirect == null) {
			return url;
		}
		return url + "&state=" + redirect;
	}
}

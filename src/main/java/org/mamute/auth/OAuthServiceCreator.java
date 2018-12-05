package org.mamute.auth;

import java.util.Random;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.mamute.qualifiers.Facebook;
import org.mamute.qualifiers.Google;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.apis.FacebookApi;
/*import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.builder.api.Google2Api;
import org.scribe.oauth.OAuthService;*/
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;

import br.com.caelum.vraptor.environment.Environment;
public class OAuthServiceCreator {
	
	public static final String FACEBOOK_APP_SECRET = "facebook.app_secret";
	public static final String FACEBOOK_REDIRECT_URI = "facebook.redirect_uri";
	public static final String FACEBOOK_CLIENT_ID = "facebook.app_id";

	private static final String GOOGLE_CLIENT_ID = "google.client_id";
	private static final String GOOGLE_CLIENT_SECRET = "google.client_secret";
	private static final String GOOGLE_REDIRECT_URI = "google.redirect_uri";
	
	private OAuth20Service service;
	
	private Environment env;
	
	@Deprecated
	public OAuthServiceCreator(){}

	@Inject
	public OAuthServiceCreator(Environment env) {
		this.env = env;
	}
	
	@PostConstruct
	public void create() {
	}

	@Produces
	@Facebook
	public OAuth20Service getInstanceFacebook() {
		final String secretState = "secret" + new Random().nextInt(999_999);
		this.service = new ServiceBuilder(env.get(FACEBOOK_CLIENT_ID))
		.state(secretState)
		.apiSecret(env.get(FACEBOOK_APP_SECRET))
		.callback(env.get("host")+env.get(FACEBOOK_REDIRECT_URI))
		.build(FacebookApi.instance());
		return service;
	}
	
	@Produces
	@Google
	public OAuth20Service getInstanceGoogle() {
		this.service = new ServiceBuilder(env.get(GOOGLE_CLIENT_ID))
		.apiSecret(env.get(GOOGLE_CLIENT_SECRET))
		.callback(env.get("host")+env.get(GOOGLE_REDIRECT_URI))
		.scope("profile email openid")
		.build(GoogleApi20.instance());
		return service;
	}
}

package org.mamute.auth;

import javax.inject.Inject;

import br.com.caelum.vraptor.environment.Property;
import org.mamute.qualifiers.Facebook;
/*import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;*/
import com.github.scribejava.core.oauth.OAuth20Service;

public class FacebookAuthService {
	/*private static final Token EMPTY_TOKEN = null;*/
	@Inject @Facebook private OAuth20Service service;

	@Property(defaultValue = "email,user_location")
	@Inject
	private String facebookScopes;
	
	public String getOauthUrl(String state) {
		String url = service.getAuthorizationUrl() + "&scope=" + facebookScopes;
		if (state == null) {
			return url;
		}
		return url + "&state=" + state;
	}
}

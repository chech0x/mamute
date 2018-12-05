package org.mamute.auth;

import static org.mamute.model.SanitizedText.fromTrustedText;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.mamute.model.MethodType;

import com.github.scribejava.core.model.OAuth2AccessToken;
/*import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;*/
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.common.base.Optional;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class GoogleAPI implements SocialAPI{
	private OAuth20Service service;
	private OAuth2AccessToken accessToken;
	private Logger LOG = Logger.getLogger(GoogleAPI.class);
	
	public GoogleAPI(OAuth2AccessToken accessToken, OAuth20Service service) {
		this.accessToken = accessToken;
		this.service = service;
	}
	
	public Optional<SignupInfo> getSignupInfo() {
		try {
			JsonObject jsonObject = new JsonParser().parse(makeRequest(getAccessToken()).getBody()).getAsJsonObject();
			LOG.info("getSignupInfo-jsonObject"+jsonObject.toString());
			String email = jsonObject.get("emails").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
			String name = jsonObject.get("displayName").getAsString();
			String photoUrl = jsonObject.get("image").getAsJsonObject().get("url").getAsString();
			
			SignupInfo signupInfo = new SignupInfo(MethodType.GOOGLE, email, fromTrustedText(name), "", photoUrl);
			return Optional.of(signupInfo);
		} catch (JsonSyntaxException e) {
			LOG.error("Error in JSon parsing - getSignupInfo", e);
		} catch (IOException e) {
			LOG.error("Error trying to get SignupInfo",e);
		}
		//TODO mejorar el control de estas excepciones
		return null;
	}
	
	private Response makeRequest(OAuth2AccessToken accessToken) {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/plus/v1/people/me");
		service.signRequest(accessToken, request);
		request.addHeader("GData-Version", "3.0");
		try {
			return service.execute(request);
		} catch (InterruptedException | ExecutionException | IOException e) {
			LOG.error("Error in OAuth service request", e);
			//TODO mejorar el control de estas excepciones
			return null;
		}
	}

	public OAuth2AccessToken getAccessToken() {
		return accessToken;
	}
}

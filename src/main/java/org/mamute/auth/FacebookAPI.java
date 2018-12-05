package org.mamute.auth;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/*import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
*/
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import org.apache.log4j.Logger;

import com.google.common.base.Optional;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class FacebookAPI implements SocialAPI{

	private final OAuth2AccessToken accessToken;
	private final OAuth20Service service;
	private Logger LOG = Logger.getLogger(GoogleAPI.class);

	public FacebookAPI(OAuth20Service service, OAuth2AccessToken accessToken) {
		this.service = service;
		this.accessToken = accessToken;
	}

	public Optional<SignupInfo> getSignupInfo() {
		String url = "https://graph.facebook.com/me?fields=name,email,location,id";
		Response response = makeRequest(url);
		JsonObject jsonObject;
		try {
			jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
			return SignupInfo.fromFacebook(jsonObject);
		} catch (JsonSyntaxException | IOException e) {
			//TODO mejorar el control de estas excepciones
			LOG.error("Error when trying to get body from response", e);
		}
		return null;
	}

	public String getUserId() {
		String url = "https://graph.facebook.com/me?fields=id";
		Response response = makeRequest(url);
		String body;
		try {
			body = response.getBody();
			JsonObject jsonObj = new JsonParser().parse(body).getAsJsonObject();
			JsonElement jsonElement = jsonObj.get("id");
			if (jsonElement == null) {
				throw new IllegalArgumentException("facebook did not sent data requested! response body: " + body);
			}
			return jsonElement.getAsString();
		} catch (IOException e) {
			//TODO mejorar el control de estas excepciones
			LOG.error("Error when trying to get body from response", e);
		}
		return null;

	}

	private Response makeRequest(String url) {
		OAuthRequest request = new OAuthRequest(Verb.GET, url);
		service.signRequest(accessToken, request);
		try {
			Response response = service.execute(request);
			String body = response.getBody();
			if (response.getCode() / 100 != 2) {
				throw new IllegalArgumentException("http error: " + response.getCode() + ", facebook response body: " + body);
			}
			return response;
		} catch (InterruptedException | ExecutionException | IOException e) {
			LOG.error("Error in OAuth service request", e);
			//TODO mejorar el control de estas excepciones
			return null;
		}

	}

	@Override
	public OAuth2AccessToken getAccessToken() {
		return accessToken;
	}
}

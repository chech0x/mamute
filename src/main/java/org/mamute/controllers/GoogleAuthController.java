package org.mamute.controllers;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.mamute.auth.GoogleAPI;
import org.mamute.auth.SocialAPI;
import org.mamute.model.MethodType;
import org.mamute.qualifiers.Google;
import org.mamute.validators.UrlValidator;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.apache.log4j.Logger;
import br.com.caelum.brutauth.auth.annotations.Public;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;

@Public
@Controller
public class GoogleAuthController extends BaseController{

	@Inject @Google private OAuth20Service service;
	@Inject private UrlValidator urlValidator;
	@Inject private LoginMethodManager loginManager;
	private Logger LOG = Logger.getLogger(GoogleAuthController.class);

	@Get("/sign-up/google/")
	public void signUpViaGoogle(String state, String code) {
		LOG.info("[/sign-up/google/] state:["+state+"] code:["+code+"]");
		OAuth2AccessToken token;
		try {
			token = service.getAccessToken(code);
		} catch (IOException | InterruptedException | ExecutionException e) {
			//TODO mejorar el control de estas excepciones
			LOG.error("Error when trying to get access token",e);
			token=null;
		}
		SocialAPI googleAPI = new GoogleAPI(token, service);

		loginManager.merge(MethodType.GOOGLE, googleAPI);

	    redirectToRightUrl(state);
	}

	private void redirectToRightUrl(String state) {
		boolean valid = urlValidator.isValid(state);
		if (!valid) {
			includeAsList("mamuteMessages", i18n("error", "error.invalid.url", state));
		}
        if (state != null && !state.isEmpty() && valid) {
            redirectTo(state);
        } else {
            redirectTo(ListController.class).home(null);
        }
	}
}

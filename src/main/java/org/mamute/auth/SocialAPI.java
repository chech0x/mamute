package org.mamute.auth;



import com.github.scribejava.core.model.OAuth2AccessToken;
import com.google.common.base.Optional;

public interface SocialAPI {
	public Optional<SignupInfo> getSignupInfo();
	public OAuth2AccessToken getAccessToken();
}

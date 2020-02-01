package com.start.secutity;

import com.start.entities.LoginToken;
import com.start.entities.Role;
import com.start.services.LoginTokenService;
import com.start.utils.RemoteIpHelper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import lombok.val;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.credential.RememberMeCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.RememberMeIdentityStore;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;

@Log
@ApplicationScoped
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Inject))
public class CustomRememberMeIdentityStore implements RememberMeIdentityStore {

    private HttpServletRequest request;
    private LoginTokenService loginTokenService;

    @Override
    public CredentialValidationResult validate(RememberMeCredential rememberMeCredential) {

        val token = rememberMeCredential.getToken();
        val loginToken = loginTokenService.findByHashAndType(token, LoginToken.TokenType.REMEMBER_ME);

        return loginToken
                .map(LoginToken::getUser)
                .map(user -> new CredentialValidationResult(
                                user.getEmail(),
                                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
                        )
                )
                .orElse(INVALID_RESULT);
    }

    @Override
    public String generateLoginToken(CallerPrincipal callerPrincipal, Set<String> set) {

        val ipAddress = RemoteIpHelper.getRemoteIpFrom(request);
        val description = String.format(
                "Remember me session for %s on %s", ipAddress, request.getHeader(HttpHeaders.USER_AGENT)
        );

        return loginTokenService.generate(
                callerPrincipal.getName(),
                ipAddress,
                description,
                LoginToken.TokenType.REMEMBER_ME
        );
    }

    @Override
    public void removeLoginToken(String loginToken) {
        loginTokenService.remove(loginToken);
    }
}

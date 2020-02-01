package com.start.secutity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.*;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;

@AutoApplySession
@ApplicationScoped
@Log
@RememberMe(
        cookieName = "__REMEMBERME_ID",
        cookieSecureOnly = false, // Remove this when login is served over HTTPS.
        cookieMaxAgeSeconds = 60 * 60 * 24 * 14) // 14 days.
@LoginToContinue(
        loginPage = "/",
        errorPage = "",
        useForwardToLogin = false
)
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Inject))
public class CustomAuthentication implements HttpAuthenticationMechanism {

    private static final String nameParam = "email";
    private static final String passwordParam = "password";

    private IdentityStoreHandler identityStoreHandler;

    @SneakyThrows
    @Override
    public AuthenticationStatus validateRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpMessageContext context
    ) throws AuthenticationException {

        Credential credential = context.getAuthParameters().getCredential();
        if (credential != null) {
            return context.doNothing();
        }

        if (request.getParameter(nameParam) != null && request.getParameter(passwordParam) != null) {

            // Get the (caller) name and password from the request
            // NOTE: This is for the smallest possible example only. In practice
            // putting the password in a request query parameter is highly
            // insecure
            String name = request.getParameter(nameParam);
            Password password = new Password(request.getParameter(passwordParam));

            // Delegate the {credentials in -> identity data out} function to
            // the Identity Store
            CredentialValidationResult result = identityStoreHandler.validate(
                    new UsernamePasswordCredential(name, password));

            if (result.getStatus() == VALID) {
                // Communicate the details of the authenticated user to the
                // container. In many cases the underlying handler will just store the details
                // and the container will actually handle the login after we return from
                // this method.
                return context.notifyContainerAboutLogin(
                        result.getCallerPrincipal(), result.getCallerGroups());

            } else {
                return context.responseUnauthorized();
            }
        }

        if (request.getRequestURI().contains("/auth/login")) {
            return context.doNothing();
        }

        return context.redirect("/auth/login");
    }
}

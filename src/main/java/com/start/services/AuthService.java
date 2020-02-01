package com.start.services;

import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.Credential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthenticationStatus login(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationParameters parameters
    );
}

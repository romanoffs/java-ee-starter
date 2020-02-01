package com.start.services.impl;

import com.start.services.AuthService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Stateless
@Local(AuthService.class)
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Inject))
public class AuthServiceImpl implements AuthService {

    private SecurityContext securityContext;

    @Override
    public AuthenticationStatus login(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationParameters parameters
    ) {

        return securityContext.authenticate(
                request,
                response,
                parameters
        );
    }
}

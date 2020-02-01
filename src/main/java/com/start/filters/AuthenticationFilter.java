package com.start.filters;

import com.start.utils.ApiResponse;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Method method = resourceInfo.getResourceMethod();

        // @DenyAll on the method takes precedence over @RolesAllowed and @PermitAll
        if (method.isAnnotationPresent(DenyAll.class)) {
            refuseRequest(requestContext);
        }

        // @RolesAllowed on the method takes precedence over @PermitAll
        RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);
        if (rolesAllowed != null) {
            performAuthorization(rolesAllowed.value(), requestContext);
            return;
        }

        // @PermitAll on the method takes precedence over @RolesAllowed on the class
        if (method.isAnnotationPresent(PermitAll.class)) {
            // Do nothing
            return;
        }

        // @DenyAll can't be attached to classes

        // @RolesAllowed on the class takes precedence over @PermitAll on the class
        rolesAllowed = resourceInfo.getResourceClass().getAnnotation(RolesAllowed.class);
        if (rolesAllowed != null) {
            performAuthorization(rolesAllowed.value(), requestContext);
        }

        // @PermitAll on the class
        if (resourceInfo.getResourceClass().isAnnotationPresent(PermitAll.class)) {
            // Do nothing
            return;
        }

        // Authentication is required for non-annotated methods
        if (!isAuthenticated(requestContext)) {
            refuseRequest(requestContext);
        }
    }

    /**
     * Perform authorization based on roles.
     *
     * @param rolesAllowed
     * @param requestContext
     */
    private void performAuthorization(String[] rolesAllowed, ContainerRequestContext requestContext) {

        if (rolesAllowed.length > 0 && !isAuthenticated(requestContext)) {
            refuseRequest(requestContext);
        }

        for (final String role : rolesAllowed) {
            if (requestContext.getSecurityContext().isUserInRole(role)) {
                return;
            }
        }

        refuseRequest(requestContext);
    }

    /**
     * Check if the user is authenticated.
     *
     * @param requestContext
     * @return isAuthenticated user
     */
    private boolean isAuthenticated(final ContainerRequestContext requestContext) {
        // Return true if the user is authenticated or false otherwise
        return requestContext.getSecurityContext().getUserPrincipal() != null;
    }

    /**
     * Refuse the request.
     */
    private void refuseRequest(final ContainerRequestContext requestContext) {
        requestContext.abortWith(
                Response.status(Response.Status.FORBIDDEN)
                        .type(MediaType.APPLICATION_JSON_TYPE)
                        .entity(ApiResponse.accessDenied())
                        .build()
        );
    }
}

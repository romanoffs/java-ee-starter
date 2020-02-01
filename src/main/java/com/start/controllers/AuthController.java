package com.start.controllers;

import com.start.services.AuthService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.val;
import org.hibernate.validator.constraints.SafeHtml;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import static javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;
import static javax.ws.rs.core.MediaType.*;

@Path("/auth")
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Inject))
@PermitAll
public class AuthController {

    private AuthService authService;

//    @Inject
//    @Param(name = "continue")
//    private boolean loginToContinue;

    @GET
    @Path("login")
    @Controller
    @Produces(TEXT_HTML)
    public String login() {
        return "auth/login";
    }

    @POST
    @Path("login")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Produces(TEXT_PLAIN)
    public Response loginIn(
            @FormParam("rememberMe")
            @NotNull Boolean rememberMe,
            @FormParam("email")
            @NotBlank
            @Email
            @Size(min = 2, max = 255) String email,
            @FormParam("password")
            @NotBlank
            @Size(min = 6, max = 255)
            @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE) String password,
            @Context HttpServletResponse response,
            @Context HttpServletRequest request
    ) {
        val parameters = withParams()
                .newAuthentication(true)
                .rememberMe(rememberMe)
                .credential(new UsernamePasswordCredential(email, new Password(password)));

        authService.login(
                request,
                response,
                parameters
        );

        // Redirect after SUCCESS Login
        return Response.status(Response.Status.FOUND).header(HttpHeaders.LOCATION, "/").build();
    }

    @GET
    @Path("logout")
    public Response logout(
            @Context HttpServletResponse response,
            @Context HttpServletRequest request
    ) throws ServletException {
        val session = request.getSession();
        session.invalidate();
        request.logout();

        // Redirect after SUCCESS logout
        return Response.status(Response.Status.FOUND).header(HttpHeaders.LOCATION, "/auth/login").build();
    }
}

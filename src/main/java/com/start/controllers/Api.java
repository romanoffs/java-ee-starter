package com.start.controllers;

import com.start.annotations.AuthUser;
import com.start.annotations.PasswordHashA;
import com.start.entities.User;
import com.start.services.UserService;
import lombok.NoArgsConstructor;
import lombok.val;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.PasswordHash;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/api")
@NoArgsConstructor
public class Api {

    private UserService userService;
    private PasswordHash passwordHash;
    private User user;

    @Inject
    public Api(
            UserService userService,
            @PasswordHashA PasswordHash passwordHash,
            @AuthUser User user) {
        this.user = user;
        this.userService = userService;
        this.passwordHash = passwordHash;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "USER"})
    public Response api() {

//        val map = Maps.newHashMap();
//        map.put("date", ZonedDateTime.now());

        return Response.ok(user).build();
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<User> users() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/create-user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response users(
            @QueryParam("name") String name,
            @QueryParam("email") String email,
            @QueryParam("password") String password
    ) {

        val user = User.builder()
                .name(name)
                .email(email)
                .password(passwordHash.generate(password.toCharArray()))
                .build();

        return Response.ok(userService.save(user)).build();
    }
}

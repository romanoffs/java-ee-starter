package com.start.config;

import com.start.annotations.AuthUser;
import com.start.entities.User;
import com.start.exceptions.ResourceNotFound;
import com.start.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.val;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.SecurityContext;

@ApplicationScoped
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Inject))
public class AuthUserProducer {

    private SecurityContext securityContext;
    private UserService userService;

    @Produces
    @SessionScoped
    @AuthUser
    @Named("authUser")
    public User getAuthUser() {
        val principal = securityContext.getCallerPrincipal();
        if (principal != null) {
            val email = principal.getName();
            return userService.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFound("User not found"));
        }
        return null;
    }
}

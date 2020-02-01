package com.start.secutity;

import com.start.annotations.PasswordHashA;
import com.start.entities.Role;
import com.start.services.UserService;
import lombok.val;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.identitystore.PasswordHash;
import java.util.stream.Collectors;

import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;

@ApplicationScoped
public class CustomIdentityStore implements IdentityStore {

    private final UserService userService;
    private final PasswordHash passwordHash;

    @Inject
    public CustomIdentityStore(
            UserService userService,
            @PasswordHashA PasswordHash passwordHash
    ) {
        this.userService = userService;
        this.passwordHash = passwordHash;
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            val cred = (UsernamePasswordCredential) credential;
            val password = cred.getPasswordAsString();
            val caller = cred.getCaller();

            val user = userService.findByEmail(caller)
                    .orElse(null);

            if (user == null) return INVALID_RESULT;

            if (caller.equals(user.getEmail()) && passwordHash.verify(password.toCharArray(), user.getPassword())) {
                return new CredentialValidationResult(
                        user.getEmail(),
                        user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
                );
            }
            return INVALID_RESULT;
        }

        System.out.println("CHECK LiteWeightIdentityStore");
        return INVALID_RESULT;
    }
}

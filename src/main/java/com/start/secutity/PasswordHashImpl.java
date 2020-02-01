package com.start.secutity;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.start.annotations.PasswordHashA;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.identitystore.PasswordHash;
import java.security.SecureRandom;
import java.util.Map;

@ApplicationScoped
@PasswordHashA
@Log
public class PasswordHashImpl implements PasswordHash {

    @Override
    public void initialize(Map<String, String> parameters) {
    }

    @Override
    public String generate(char[] chars) {
        return BCrypt.with(new SecureRandom()).hashToString(12, chars);
    }

    @Override
    public boolean verify(char[] chars, String s) {
        return BCrypt.verifyer().verify(chars, s).verified;
    }
}

package com.template.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class HttpUtils {
    public static boolean isLogged() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication == null || authentication instanceof AnonymousAuthenticationToken);
    }
}
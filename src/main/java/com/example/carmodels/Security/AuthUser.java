package com.example.carmodels.Security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.example.carmodels.Constants.AuthorityConst.*;

@Component
public class AuthUser {
    public boolean isUserLogged() {
        SecurityContext context = SecurityContextHolder.getContext();

        for (GrantedAuthority authority : context.getAuthentication().getAuthorities()) {
            return authority.getAuthority().equals(REGULAR_USER)
                    || authority.getAuthority().equals(GOOGLE_OAUTH2_USER)
                    || authority.getAuthority().equals(GITHUB_OAUTH2_USER);
        }
        return false;
    }
}

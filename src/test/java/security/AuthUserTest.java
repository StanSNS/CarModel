package security;

import com.example.carmodels.Security.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;

import static com.example.carmodels.Constants.AuthorityConst.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthUserTest {

    private AuthUser authUser;

    @BeforeEach
    public void setUp() {
        authUser = new AuthUser();
    }

    @Test
    public void testIsUserLogged_WhenRegularUserIsLogged() {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken("user", "password", REGULAR_USER);
        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(authentication);

        assertTrue(authUser.isUserLogged());
    }

    @Test
    public void testIsUserLogged_WhenGoogleOAuth2UserIsLogged() {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken("user", "password", GOOGLE_OAUTH2_USER);
        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(authentication);

        assertTrue(authUser.isUserLogged());
    }

    @Test
    public void testIsUserLogged_WhenGithubOAuth2UserIsLogged() {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken("user", "password", GITHUB_OAUTH2_USER);
        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(authentication);

        assertTrue(authUser.isUserLogged());
    }

    @Test
    public void testIsUserLogged_WhenNoUserIsLogged() {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken("user", "password", Collections.emptyList());
        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(authentication);

        assertFalse(authUser.isUserLogged());
    }
}

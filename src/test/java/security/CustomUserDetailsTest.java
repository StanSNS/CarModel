package security;

import com.example.carmodels.Models.Entity.RoleModels;
import com.example.carmodels.Models.Entity.UserModels;
import com.example.carmodels.Security.CustomUserDetails;
import com.example.carmodels.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CustomUserDetailsTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        RoleModels role = new RoleModels();
        role.setName("ROLE_USER");
        Set<RoleModels> roles = new HashSet<>();
        roles.add(role);
        UserModels userModels = new UserModels();
        userModels.setEmail(email);
        userModels.setPassword(password);
        userModels.setRoles(roles);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userModels));

        // Act
        UserDetails userDetails = customUserDetails.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Arrange
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NullPointerException.class, () -> customUserDetails.loadUserByUsername(email));
    }

    @Test
    void loadUserByUsername_UserWithNoRoles_ReturnsUserDetails() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        UserModels userModels = new UserModels();
        userModels.setEmail(email);
        userModels.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userModels));

        // Act
        UserDetails userDetails = customUserDetails.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }
}

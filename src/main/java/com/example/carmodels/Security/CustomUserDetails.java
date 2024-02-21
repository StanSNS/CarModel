package com.example.carmodels.Security;

import com.example.carmodels.Models.Entity.RoleModels;
import com.example.carmodels.Models.Entity.UserModels;
import com.example.carmodels.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.example.carmodels.constants.RoleConst.ROLE_PREFIX;

/**
 * Service responsible for loading user details and creating default user details when necessary.
 */
@Service
public class CustomUserDetails implements UserDetailsService {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final UserRepository userRepository;

    public CustomUserDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Load user details by email from the repository
        UserModels userModels = userRepository.findByEmail(email).orElse(null);

        // Extract and build user authorities from roles
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (userModels.getRoles() != null) {
            for (RoleModels role : userModels.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getName()));
            }
        }
        return new User(email, userModels.getPassword(), authorities);
    }
}




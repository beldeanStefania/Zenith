package com.ubb.zenith.service;

import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User.UserBuilder;;
/**
 * Implementation of the {@link UserDetailsService} interface to load user-specific data.
 * This service is used for authentication and retrieving user details.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    /**
     * Constructor for injecting dependencies.
     *
     * @param userRepository the repository to interact with user data.
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * Locates the user based on the username.
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated {@link UserDetails} instance.
     * @throws UsernameNotFoundException if the user could not be found or has no granted authorities.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(username);
        builder.password(user.getPassword());
        builder.roles(user.getRole().getName());
        return builder.build();
    }
}

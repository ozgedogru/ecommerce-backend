package com.workintech.ecommerce.service;

import com.workintech.ecommerce.config.JwtTokenUtil;
import com.workintech.ecommerce.dto.AuthenticationResponseDto;
import com.workintech.ecommerce.entity.ApplicationUser;
import com.workintech.ecommerce.entity.Role;
import com.workintech.ecommerce.entity.Store;
import com.workintech.ecommerce.exception.UserException;
import com.workintech.ecommerce.repository.RoleRepository;
import com.workintech.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    public ApplicationUser register(String fullName, String email, String password, Long roleId, Store store) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException("Email already in use.", HttpStatus.CONFLICT);
        }

        return saveUser(fullName, email, password, roleId, store);
    }


    public ResponseEntity<AuthenticationResponseDto> login(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            final String token = jwtTokenUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new AuthenticationResponseDto(token));

        } catch (UsernameNotFoundException ex) {
            throw new UserException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            throw new UserException("An error occurred during login", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ApplicationUser saveUser(String fullName, String email, String password, Long roleId, Store store) {
        String encodedPassword = passwordEncoder.encode(password);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        ApplicationUser user = new ApplicationUser();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(encodedPassword);
        user.setRole(role);

        if (role.getId() == 2 && store != null) {
            user.setStore(store);
            store.setUser(user);
        }

        return userRepository.save(user);
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}

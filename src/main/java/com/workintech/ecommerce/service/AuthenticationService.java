package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.ApplicationUser;
import com.workintech.ecommerce.entity.Role;
import com.workintech.ecommerce.repository.RoleRepository;
import com.workintech.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApplicationUser register(String fullName, String email, String password, String address, String creditCard) {
        String encodePassword = passwordEncoder.encode(password);
        Role customerRole = roleRepository.findByAuthority("Customer").get();

        Set<Role> roles = new HashSet<>();
        roles.add(customerRole);

        ApplicationUser user = new ApplicationUser();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(encodePassword);
        user.setAddress(address);
        user.setCreditCard(creditCard);
        user.setRoles(roles);

        return userRepository.save(user);

    }

}

package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Address;
import com.workintech.ecommerce.entity.ApplicationUser;
import com.workintech.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private static UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            System.out.println("User credentials are not valid.");
            throw new UsernameNotFoundException("User credentials are not valid.");
        });
    }

    public List<ApplicationUser> getAllUsers() {
        return userRepository.findAll();
    }

    public static ApplicationUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }


    public void deleteUserById(Long id) {
        ApplicationUser user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }


    public void addAddressToUser(Long userId, Address address) {
        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        address.setUser(user);
        user.getAddresses().add(address);

        userRepository.save(user);
    }

    public List<Address> getUserAddresses(Long userId) {
        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        return user.getAddresses();
    }



}

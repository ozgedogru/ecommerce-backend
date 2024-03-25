package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Address;
import com.workintech.ecommerce.entity.ApplicationUser;
import com.workintech.ecommerce.repository.AddressRepository;
import com.workintech.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private AddressRepository addressRepository;
    private UserRepository userRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public List<Address> getAllAddressesByUserId(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    public Address createAddress(Address address, Long userId) {
        ApplicationUser user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        address.setUser(user);
        return addressRepository.save(address);
    }

    public void updateAddress(Long id, Address updatedAddress) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
        address.setAddressTitle(updatedAddress.getAddressTitle());
        address.setNameSurname(updatedAddress.getNameSurname());
        address.setPhone(updatedAddress.getPhone());
        address.setCity(updatedAddress.getCity());
        address.setDistrict(updatedAddress.getDistrict());
        address.setNeighborhood(updatedAddress.getNeighborhood());
        address.setAddressDir(updatedAddress.getAddressDir());
        addressRepository.save(address);
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}
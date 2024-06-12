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

    public Address updateAddress(Long addressId, Address updatedAddress, Long userId) {
        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId + " for user: " + userId));
        address.setAddressTitle(updatedAddress.getAddressTitle());
        address.setNameSurname(updatedAddress.getNameSurname());
        address.setPhone(updatedAddress.getPhone());
        address.setCity(updatedAddress.getCity());
        address.setDistrict(updatedAddress.getDistrict());
        address.setNeighborhood(updatedAddress.getNeighborhood());
        address.setAddressDir(updatedAddress.getAddressDir());
        return addressRepository.save(address);
    }

    public void deleteAddress(Long addressId) {
        Optional<Address> addressOptional = addressRepository.findById(addressId);
        if (addressOptional.isPresent()) {
            addressRepository.delete(addressOptional.get());
        } else {
            throw new RuntimeException("Address not found with id: " + addressId);
        }
    }
}
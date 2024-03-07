package com.workintech.ecommerce.util;

import com.workintech.ecommerce.dto.AddressDto;
import com.workintech.ecommerce.entity.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressDtoConversion {

    public static AddressDto convertAddress(Address address) {
        return new AddressDto(
                address.getId(),
                address.getAddressTitle(),
                address.getNameSurname(),
                address.getPhone(),
                address.getCity(),
                address.getDistrict(),
                address.getNeighborhood(),
                address.getAddressDir());
    }

    public static List<AddressDto> convertAddressList(List<Address> addresses) {
        List<AddressDto> addressDtos = new ArrayList<>();
        addresses.forEach(address ->
                addressDtos.add(new AddressDto(
                        address.getId(),
                        address.getAddressTitle(),
                        address.getNameSurname(),
                        address.getPhone(),
                        address.getCity(),
                        address.getDistrict(),
                        address.getNeighborhood(),
                        address.getAddressDir())));
        return addressDtos;
    }
}

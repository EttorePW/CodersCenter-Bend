package com.coderscenter.backend.mapper;

import com.coderscenter.backend.dtos.address.response.ResponseAddressDTO;
import com.coderscenter.backend.entities.profile.Address;
import org.springframework.stereotype.Service;

@Service
public class AddressMapper {

    public ResponseAddressDTO toResponseDTO(Address address) {
        return ResponseAddressDTO.builder()
                .addressId(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .zip(address.getZip())
                .build();
    }

}

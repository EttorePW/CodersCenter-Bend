package com.coderscenter.backend.services;

import com.coderscenter.backend.dtos.address.request.RequestAddressDTO;
import com.coderscenter.backend.dtos.address.response.ResponseAddressDTO;
import com.coderscenter.backend.entities.profile.Address;
import com.coderscenter.backend.exceptions.ResourceNotFoundException;
import com.coderscenter.backend.mapper.AddressMapper;
import com.coderscenter.backend.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    /**
     * Get all addresses from the database
     * @return list of {@link ResponseAddressDTO}
     */
    public List<ResponseAddressDTO> getAllAddresses() {
        return addressRepository.findAll()
                .stream()
                .map(addressMapper::toResponseDTO)
                .toList();
    }

    /**
     * Get address by id
     * @param addressId id of the address
     * @return {@link ResponseAddressDTO}
     * @throws ResourceNotFoundException if address with the given id does not exist
     */
    public ResponseAddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Addresse mit der ID " + addressId + " nicht gefunden"));
        return addressMapper.toResponseDTO(address);
    }

    /**
     * Create a new address
     * @param requestAddressDTO DTO with the address details
     * @return {@link ResponseAddressDTO} of the created address
     */
    public ResponseAddressDTO createNew(RequestAddressDTO requestAddressDTO) {
        Address address =  Address.builder()
                .street(requestAddressDTO.getStreet())
                .city(requestAddressDTO.getCity())
                .zip(requestAddressDTO.getZip())
                .build();

        addressRepository.save(address);

        return addressMapper.toResponseDTO(address);
    }

    /**
     * Update an existing address partially or fully by id
     * @param addressId id of the address to be updated
     * @param requestAddressDTO DTO with the address details to be updated
     * @return {@link ResponseAddressDTO} of the updated address
     * @throws ResourceNotFoundException if address with the given id does not exist
     */
    public ResponseAddressDTO updateAddress (Long addressId, RequestAddressDTO requestAddressDTO) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Adresse mit der ID " + addressId + " nicht gefunden"));

        if (requestAddressDTO.getStreet() != null) {
            address.setStreet(requestAddressDTO.getStreet());
        }
        if (requestAddressDTO.getCity() != null) {
            address.setCity(requestAddressDTO.getCity());
        }
        if (requestAddressDTO.getZip() != null) {
            address.setZip(requestAddressDTO.getZip());
        }

        addressRepository.save(address);
        return addressMapper.toResponseDTO(address);
    }

    /**
     * Delete an address by id
     * @param addressId id of the address to be deleted
     * @throws ResourceNotFoundException if address with the given id does not exist
     */
    public void deleteAddressById (Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Adresse mit der ID " + addressId + " nicht gefunden"));
        addressRepository.delete(address);
    }
}

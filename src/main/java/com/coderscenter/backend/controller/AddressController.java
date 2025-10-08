package com.coderscenter.backend.controller;

import com.coderscenter.backend.dtos.address.request.RequestAddressDTO;
import com.coderscenter.backend.dtos.address.response.ResponseAddressDTO;
import com.coderscenter.backend.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    /**
     * Get all addresses
     * @return list of {@link ResponseAddressDTO}
     */
    @GetMapping
    public ResponseEntity<List<ResponseAddressDTO>> getAll() {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    /**
     * Get address by id
     * @param addressId id of the address
     * @return {@link ResponseAddressDTO}
     */
    @GetMapping("/{addressId}")
    public ResponseEntity<ResponseAddressDTO> getById(@PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.getAddressById(addressId));
    }

    /**
     * Create a new address
     * @param requestAddressDTO DTO with the address details
     * @return {@link ResponseAddressDTO} of the created address
     */
    @PostMapping
    public ResponseEntity<ResponseAddressDTO> postNew(@Valid @RequestBody RequestAddressDTO requestAddressDTO) {
        return new ResponseEntity<>(addressService.createNew(requestAddressDTO), HttpStatus.CREATED);
    }

    /**
     * Update an existing address
     * @param id id of the address to update
     * @param requestAddressDTO DTO with the new address details
     * @return {@link ResponseAddressDTO} of the updated address
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseAddressDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid RequestAddressDTO requestAddressDTO) {
        return ResponseEntity.ok(addressService.updateAddress(id, requestAddressDTO));
    }

    /**
     * Delete address by id
     * @param addressId id of the address to delete
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> delete(@PathVariable Long addressId) {
        addressService.deleteAddressById(addressId);
        return ResponseEntity.noContent().build();
    }
}

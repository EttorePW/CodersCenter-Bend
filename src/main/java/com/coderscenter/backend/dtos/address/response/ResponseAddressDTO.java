package com.coderscenter.backend.dtos.address.response;

import lombok.*;

@Builder
@Data
public class ResponseAddressDTO {

    private Long addressId;
    private String street;
    private String city;
    private String zip;

}

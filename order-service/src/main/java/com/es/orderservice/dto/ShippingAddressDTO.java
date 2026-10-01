package com.es.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingAddressDTO {
    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "County is required")
    private String county;

    @NotBlank(message = "Postcode is required")
    private String postcode;

    @NotBlank(message = "Country is required")
    private String country;
}

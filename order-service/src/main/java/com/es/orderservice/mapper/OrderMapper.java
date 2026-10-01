package com.es.orderservice.mapper;

import com.es.orderservice.dto.OrderItemResponseDTO;
import com.es.orderservice.dto.OrderResponseDTO;
import com.es.orderservice.dto.ShippingAddressDTO;
import com.es.orderservice.model.Order;
import com.es.orderservice.model.OrderItem;
import com.es.orderservice.model.ShippingAddress;

public class OrderMapper {
    private OrderMapper() {}

    public static OrderResponseDTO toDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();

        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingAddress(toShippingAddressDTO(order.getShippingAddress()));
        dto.setItems(order.getItems().stream().map(OrderMapper::toItemDTO).toList());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        return dto;
    }

    public static ShippingAddress toShippingAddress(ShippingAddressDTO dto) {
        return ShippingAddress.builder()
                .street(dto.getStreet())
                .city(dto.getCity())
                .county(dto.getCounty())
                .postcode(dto.getPostcode())
                .country(dto.getCountry())
                .build();
    }

    private static ShippingAddressDTO toShippingAddressDTO(ShippingAddress address) {
        ShippingAddressDTO dto = new ShippingAddressDTO();

        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setCounty(address.getCounty());
        dto.setPostcode(address.getPostcode());
        dto.setCountry(address.getCountry());

        return dto;
    }

    private static OrderItemResponseDTO toItemDTO(OrderItem item) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();

        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getSubtotal());

        return dto;
    }
}

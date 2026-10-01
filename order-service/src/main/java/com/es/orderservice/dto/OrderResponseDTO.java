package com.es.orderservice.dto;

import com.es.orderservice.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderResponseDTO {
    private UUID id;
    private UUID userId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private ShippingAddressDTO shippingAddress;
    private List<OrderItemResponseDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

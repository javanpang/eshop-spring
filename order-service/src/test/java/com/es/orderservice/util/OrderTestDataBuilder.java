package com.es.orderservice.util;

import com.es.orderservice.dto.ShippingAddressDTO;
import com.es.orderservice.model.Order;
import com.es.orderservice.model.OrderItem;
import com.es.orderservice.model.OrderStatus;
import com.es.orderservice.model.ShippingAddress;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderTestDataBuilder {

    public static final UUID DEFAULT_ORDER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    public static final UUID DEFAULT_USER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    public static final UUID DEFAULT_PRODUCT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
    public static final UUID SECOND_PRODUCT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174003");

    public static final String DEFAULT_PRODUCT_NAME = "Product Name";
    public static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal("59.99");
    public static final int DEFAULT_QUANTITY = 2;
    public static final BigDecimal DEFAULT_SUBTOTAL = new BigDecimal("119.98");

    private OrderTestDataBuilder() {
    }

    public static ShippingAddress buildShippingAddress() {
        return ShippingAddress.builder()
                .street("123 Main St")
                .city("Liverpool")
                .county("Merseyside")
                .postcode("L1 2AB")
                .country("United Kingdom")
                .build();
    }

    public static ShippingAddressDTO buildShippingAddressDTO() {
        ShippingAddressDTO dto = new ShippingAddressDTO();
        dto.setStreet("123 Main St");
        dto.setCity("Liverpool");
        dto.setCounty("Merseyside");
        dto.setPostcode("L1 2AB");
        dto.setCountry("United Kingdom");
        return dto;
    }

    public static OrderItem buildOrderItem() {
        return OrderItem.builder()
                .id(UUID.randomUUID())
                .productId(DEFAULT_PRODUCT_ID)
                .productName(DEFAULT_PRODUCT_NAME)
                .unitPrice(DEFAULT_UNIT_PRICE)
                .quantity(DEFAULT_QUANTITY)
                .subtotal(DEFAULT_SUBTOTAL)
                .build();
    }

    public static OrderItem buildOrderItem(UUID productId, String name, BigDecimal price, int quantity) {
        return OrderItem.builder()
                .id(UUID.randomUUID())
                .productId(productId)
                .productName(name)
                .unitPrice(price)
                .quantity(quantity)
                .subtotal(price.multiply(BigDecimal.valueOf(quantity)))
                .build();
    }

    public static Order buildOrder() {
        Order order = Order.builder()
                .id(DEFAULT_ORDER_ID)
                .userId(DEFAULT_USER_ID)
                .status(OrderStatus.CONFIRMED)
                .totalAmount(DEFAULT_SUBTOTAL)
                .shippingAddress(buildShippingAddress())
                .createdAt(LocalDateTime.of(2026, 1, 1, 12, 0))
                .updatedAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();

        order.addItem(buildOrderItem());

        return order;
    }

    public static Order buildOrderWithMultipleItems() {
        Order order = Order.builder()
                .id(DEFAULT_ORDER_ID)
                .userId(DEFAULT_USER_ID)
                .status(OrderStatus.CONFIRMED)
                .totalAmount(new BigDecimal("298.97"))
                .shippingAddress(buildShippingAddress())
                .createdAt(LocalDateTime.of(2026, 1, 1, 12, 0))
                .updatedAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();

        order.addItem(buildOrderItem(DEFAULT_PRODUCT_ID, DEFAULT_PRODUCT_NAME, DEFAULT_UNIT_PRICE, DEFAULT_QUANTITY));
        order.addItem(buildOrderItem(SECOND_PRODUCT_ID, "Second Product", new BigDecimal("49.99"), 1));

        return order;
    }

    public static Order buildOrderWithNoItems() {
        return Order.builder()
                .id(DEFAULT_ORDER_ID)
                .userId(DEFAULT_USER_ID)
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .shippingAddress(buildShippingAddress())
                .createdAt(LocalDateTime.of(2026, 1, 1, 12, 0))
                .updatedAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();
    }
}

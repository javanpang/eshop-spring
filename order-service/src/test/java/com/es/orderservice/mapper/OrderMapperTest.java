package com.es.orderservice.mapper;

import com.es.orderservice.dto.OrderItemResponseDTO;
import com.es.orderservice.dto.OrderResponseDTO;
import com.es.orderservice.dto.ShippingAddressDTO;
import com.es.orderservice.model.Order;
import com.es.orderservice.model.OrderItem;
import com.es.orderservice.model.OrderStatus;
import com.es.orderservice.model.ShippingAddress;
import com.es.orderservice.util.OrderTestDataBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderMapperTest {

    // --- toDTO ---

    @Test
    void toDTO_mapsTopLevelOrderFields() {
        Order order = OrderTestDataBuilder.buildOrder();

        OrderResponseDTO dto = OrderMapper.toDTO(order);

        assertThat(dto.getId()).isEqualTo(order.getId());
        assertThat(dto.getUserId()).isEqualTo(order.getUserId());
        assertThat(dto.getStatus()).isEqualTo(order.getStatus());
        assertThat(dto.getTotalAmount()).isEqualByComparingTo(order.getTotalAmount());
        assertThat(dto.getCreatedAt()).isEqualTo(order.getCreatedAt());
        assertThat(dto.getUpdatedAt()).isEqualTo(order.getUpdatedAt());
    }

    @Test
    void toDTO_mapsShippingAddress() {
        Order order = OrderTestDataBuilder.buildOrder();

        OrderResponseDTO dto = OrderMapper.toDTO(order);

        assertThat(dto.getShippingAddress()).isNotNull();
        assertThat(dto.getShippingAddress().getStreet()).isEqualTo(order.getShippingAddress().getStreet());
        assertThat(dto.getShippingAddress().getCity()).isEqualTo(order.getShippingAddress().getCity());
        assertThat(dto.getShippingAddress().getCountry()).isEqualTo(order.getShippingAddress().getCountry());
        assertThat(dto.getShippingAddress().getPostcode()).isEqualTo(order.getShippingAddress().getPostcode());
        assertThat(dto.getShippingAddress().getCounty()).isEqualTo(order.getShippingAddress().getCounty());
    }

    @Test
    void toDTO_mapsSingleOrderItem() {
        Order order = OrderTestDataBuilder.buildOrder();
        OrderItem expectedItem = order.getItems().getFirst();

        OrderResponseDTO dto = OrderMapper.toDTO(order);

        assertThat(dto.getItems()).hasSize(1);

        OrderItemResponseDTO itemDTO = dto.getItems().getFirst();
        assertThat(itemDTO.getId()).isEqualTo(expectedItem.getId());
        assertThat(itemDTO.getProductId()).isEqualTo(expectedItem.getProductId());
        assertThat(itemDTO.getProductName()).isEqualTo(expectedItem.getProductName());
        assertThat(itemDTO.getUnitPrice()).isEqualByComparingTo(expectedItem.getUnitPrice());
        assertThat(itemDTO.getQuantity()).isEqualTo(expectedItem.getQuantity());
        assertThat(itemDTO.getSubtotal()).isEqualByComparingTo(expectedItem.getSubtotal());
    }

    @Test
    void toDTO_mapsMultipleOrderItemsInOrder() {
        Order order = OrderTestDataBuilder.buildOrderWithMultipleItems();

        OrderResponseDTO dto = OrderMapper.toDTO(order);

        assertThat(dto.getItems()).hasSize(2);
        assertThat(dto.getItems()).extracting(OrderItemResponseDTO::getProductId).containsExactlyElementsOf(order.getItems().stream().map(OrderItem::getProductId).toList());
    }

    @Test
    void toDTO_withNoItems_returnsEmptyItemsList() {
        Order order = OrderTestDataBuilder.buildOrderWithNoItems();

        OrderResponseDTO dto = OrderMapper.toDTO(order);

        assertThat(dto.getItems()).isEmpty();
    }

    // --- toShippingAddress ---

    @Test
    void toShippingAddress_mapsAllFields() {
        ShippingAddressDTO dto = OrderTestDataBuilder.buildShippingAddressDTO();

        ShippingAddress address = OrderMapper.toShippingAddress(dto);

        assertThat(address.getStreet()).isEqualTo(dto.getStreet());
        assertThat(address.getCity()).isEqualTo(dto.getCity());
        assertThat(address.getCounty()).isEqualTo(dto.getCounty());
        assertThat(address.getPostcode()).isEqualTo(dto.getPostcode());
        assertThat(address.getCountry()).isEqualTo(dto.getCountry());
    }

    // --- shipping address round trip ---

    @Test
    void shippingAddress_roundTrip_preservesAllFields() {
        ShippingAddressDTO original = OrderTestDataBuilder.buildShippingAddressDTO();

        ShippingAddress entity = OrderMapper.toShippingAddress(original);
        Order order = Order.builder().id(OrderTestDataBuilder.DEFAULT_ORDER_ID).userId(OrderTestDataBuilder.DEFAULT_USER_ID).status(OrderStatus.PENDING).totalAmount(BigDecimal.ZERO).shippingAddress(entity).build();

        OrderResponseDTO dto = OrderMapper.toDTO(order);

        assertThat(dto.getShippingAddress().getStreet()).isEqualTo(original.getStreet());
        assertThat(dto.getShippingAddress().getCity()).isEqualTo(original.getCity());
        assertThat(dto.getShippingAddress().getCounty()).isEqualTo(original.getCounty());
        assertThat(dto.getShippingAddress().getPostcode()).isEqualTo(original.getPostcode());
        assertThat(dto.getShippingAddress().getCountry()).isEqualTo(original.getCountry());
    }
}

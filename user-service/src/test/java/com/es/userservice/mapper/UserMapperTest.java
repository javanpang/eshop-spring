package com.es.userservice.mapper;

import com.es.userservice.dto.UserResponseDTO;
import com.es.userservice.model.User;
import com.es.userservice.util.UserTestDataBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {

    @Test
    void toDTO_mapsAllFields() {
        User user = UserTestDataBuilder.buildUser();

        UserResponseDTO dto = UserMapper.toDTO(user);

        assertThat(dto.getId()).isEqualTo(user.getId());
        assertThat(dto.getEmail()).isEqualTo(user.getEmail());
        assertThat(dto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(user.getLastName());
        assertThat(dto.getRole()).isEqualTo(user.getRole()
                .name());
    }

    @Test
    void toDTO_neverMapsPassword() {
        User user = UserTestDataBuilder.buildUser();

        UserResponseDTO dto = UserMapper.toDTO(user);

        assertThat(dto).hasNoNullFieldsOrPropertiesExcept("password");
    }
}

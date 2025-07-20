package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserUpdateRequestJsonTest {

    @Autowired
    private JacksonTester<UserUpdateRequest> json;

    @Autowired
    private ObjectMapper objectMapper;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testSerialize() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("Updated Name");
        request.setEmail("updated.email@example.com");

        JsonContent<UserUpdateRequest> result = json.write(request);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Updated Name");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("updated.email@example.com");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"name\":\"Updated Name\",\"email\":\"updated.email@example.com\"}";

        UserUpdateRequest result = objectMapper.readValue(content, UserUpdateRequest.class);

        assertThat(result.getName()).isEqualTo("Updated Name");
        assertThat(result.getEmail()).isEqualTo("updated.email@example.com");
    }

    @Test
    void whenEmailIsInvalid_thenValidationFails() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("invalid-email");

        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("должно иметь формат адреса электронной почты");
    }

    @Test
    void whenBothFieldsAreNull_thenValidationPasses() {
        UserUpdateRequest request = new UserUpdateRequest();

        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

}
package ru.practicum.shareit.item.dto;

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
public class NewItemRequestJsonTest {

    @Autowired
    private JacksonTester<NewItemRequest> json;

    @Autowired
    private ObjectMapper objectMapper;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testSerialize() throws Exception {
        NewItemRequest request = new NewItemRequest();
        request.setName("Дрель");
        request.setDescription("Аккумуляторная дрель");
        request.setAvailable(true);

        JsonContent<NewItemRequest> result = json.write(request);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Дрель");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Аккумуляторная дрель");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"name\":\"Дрель\",\"description\":\"Аккумуляторная дрель\",\"available\":true}";

        NewItemRequest result = objectMapper.readValue(content, NewItemRequest.class);

        assertThat(result.getName()).isEqualTo("Дрель");
        assertThat(result.getDescription()).isEqualTo("Аккумуляторная дрель");
        assertThat(result.getAvailable()).isTrue();
    }

    @Test
    void whenNameIsBlank_thenValidationFails() {
        NewItemRequest request = new NewItemRequest();
        request.setName("");
        request.setDescription("Valid description");
        request.setAvailable(true);

        Set<ConstraintViolation<NewItemRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("Название не может быть пустым");
    }

    @Test
    void whenDescriptionIsBlank_thenValidationFails() {
        NewItemRequest request = new NewItemRequest();
        request.setName("Valid name");
        request.setDescription("");
        request.setAvailable(true);

        Set<ConstraintViolation<NewItemRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("Описание не может быть пустым");
    }

    @Test
    void whenAvailableIsNull_thenValidationFails() {
        NewItemRequest request = new NewItemRequest();
        request.setName("Valid name");
        request.setDescription("Valid description");
        request.setAvailable(null);

        Set<ConstraintViolation<NewItemRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("Статус доступности должен быть указан");
    }
}
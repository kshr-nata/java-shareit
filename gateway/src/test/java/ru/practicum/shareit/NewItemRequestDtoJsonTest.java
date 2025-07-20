package ru.practicum.shareit.request.dto;

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
public class NewItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<NewItemRequestDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testSerialize() throws Exception {
        NewItemRequestDto request = new NewItemRequestDto();
        request.setDescription("Нужна дрель для ремонта");

        JsonContent<NewItemRequestDto> result = json.write(request);

        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Нужна дрель для ремонта");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"description\":\"Нужна дрель для ремонта\"}";

        NewItemRequestDto result = objectMapper.readValue(content, NewItemRequestDto.class);

        assertThat(result.getDescription()).isEqualTo("Нужна дрель для ремонта");
    }

    @Test
    void whenDescriptionIsBlank_thenValidationFails() {
        NewItemRequestDto request = new NewItemRequestDto();
        request.setDescription("");

        Set<ConstraintViolation<NewItemRequestDto>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("Описание запроса не может быть пустым");
    }

    @Test
    void whenDescriptionIsNull_thenValidationFails() {
        NewItemRequestDto request = new NewItemRequestDto();
        request.setDescription(null);

        Set<ConstraintViolation<NewItemRequestDto>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("Описание запроса не может быть пустым");
    }
}
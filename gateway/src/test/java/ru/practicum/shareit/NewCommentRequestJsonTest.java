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
public class NewCommentRequestJsonTest {

    @Autowired
    private JacksonTester<NewCommentRequest> json;

    @Autowired
    private ObjectMapper objectMapper;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testSerialize() throws Exception {
        NewCommentRequest request = new NewCommentRequest();
        request.setText("Отличная дрель, всем рекомендую!");

        JsonContent<NewCommentRequest> result = json.write(request);

        assertThat(result).extractingJsonPathStringValue("$.text")
                .isEqualTo("Отличная дрель, всем рекомендую!");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"text\":\"Отличная дрель, всем рекомендую!\"}";

        NewCommentRequest result = objectMapper.readValue(content, NewCommentRequest.class);

        assertThat(result.getText()).isEqualTo("Отличная дрель, всем рекомендую!");
    }

    @Test
    void whenTextIsBlank_thenValidationFails() {
        NewCommentRequest request = new NewCommentRequest();
        request.setText("");

        Set<ConstraintViolation<NewCommentRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("не должно быть пустым");
    }

}
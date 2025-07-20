package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import jakarta.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<BookItemRequestDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testSerialize() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(2);

        BookItemRequestDto dto = new BookItemRequestDto();
        dto.setItemId(1);
        dto.setStart(start);
        dto.setEnd(end);

        JsonContent<BookItemRequestDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);

        // Сравниваем даты без учета наносекунд
        assertThat(result).extractingJsonPathStringValue("$.start")
                .startsWith(start.toString().substring(0, 23));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .startsWith(end.toString().substring(0, 23));
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"itemId\":1,\"start\":\"2023-12-31T12:00:00\",\"end\":\"2024-01-01T12:00:00\"}";

        BookItemRequestDto dto = objectMapper.readValue(content, BookItemRequestDto.class);

        assertThat(dto.getItemId()).isEqualTo(1);
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.of(2023, 12, 31, 12, 0));
        assertThat(dto.getEnd()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0));
    }

    @Test
    void whenStartIsInPast_thenValidationFails() {
        BookItemRequestDto dto = new BookItemRequestDto();
        dto.setItemId(1);
        dto.setStart(LocalDateTime.now().minusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(1));

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .contains("должно содержать сегодняшнее число или дату");
    }


    @Test
    void whenValidDto_thenNoValidationErrors() {
        BookItemRequestDto dto = new BookItemRequestDto();
        dto.setItemId(1);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }
}
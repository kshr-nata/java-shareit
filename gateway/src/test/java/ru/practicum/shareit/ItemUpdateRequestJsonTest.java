package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemUpdateRequestJsonTest {

    @Autowired
    private JacksonTester<ItemUpdateRequest> json;

    @Autowired
    private ObjectMapper objectMapper;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testSerialize() throws Exception {
        ItemUpdateRequest request = new ItemUpdateRequest();
        request.setName("Updated Дрель");
        request.setDescription("Обновленная дрель");
        request.setAvailable(false);

        JsonContent<ItemUpdateRequest> result = json.write(request);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Updated Дрель");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Обновленная дрель");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isFalse();
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"name\":\"Updated Дрель\",\"description\":\"Обновленная дрель\",\"available\":false}";

        ItemUpdateRequest result = objectMapper.readValue(content, ItemUpdateRequest.class);

        assertThat(result.getName()).isEqualTo("Updated Дрель");
        assertThat(result.getDescription()).isEqualTo("Обновленная дрель");
        assertThat(result.getAvailable()).isFalse();
    }
}
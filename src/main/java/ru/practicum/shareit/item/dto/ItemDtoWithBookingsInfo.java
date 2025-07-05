package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingInfo;

import java.util.Set;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@Getter
@Setter
public class ItemDtoWithBookingsInfo {
    Integer id;
    String name;
    String description;
    boolean available;
    BookingInfo lastBooking;
    BookingInfo nextBooking;
    Set<String> comments;

}

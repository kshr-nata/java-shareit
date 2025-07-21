package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentMapper {
    public static Comment mapToComment(NewCommentRequest request, User user, Item item) {
        Comment comment = new Comment();
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setText(request.getText());
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public static CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(),
                comment.getAuthor().getName(), comment.getCreated());
    }
}

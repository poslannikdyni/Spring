package com.edu.ulab.app.entity;

import com.edu.ulab.app.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBookEntity extends AbstractEntity {
    private Long userId;
    private Long bookId;

    public UserBookEntity(Long userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    public UserBookEntity(Long id, Long userId, Long bookId) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "UserBookEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", bookId=" + bookId +
                '}';
    }
}

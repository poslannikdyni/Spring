package com.edu.ulab.app.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookEntity extends AbstractEntity {
    private Long userId;
    private String title;
    private String author;
    private long pageCount;

    @Override
    public String toString() {
        return "BookEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", pageCount=" + pageCount +
                '}';
    }
}

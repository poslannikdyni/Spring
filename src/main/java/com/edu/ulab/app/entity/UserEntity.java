package com.edu.ulab.app.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserEntity extends AbstractEntity {
    private String fullName;
    private String title;
    private int age;

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", title='" + title + '\'' +
                ", age=" + age +
                '}';
    }
}

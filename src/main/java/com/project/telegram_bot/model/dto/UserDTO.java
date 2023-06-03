package com.project.telegram_bot.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserDTO {
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private Timestamp subscribedAt;
    private int level;
    private String isActive;
    private int freeLesson;
}

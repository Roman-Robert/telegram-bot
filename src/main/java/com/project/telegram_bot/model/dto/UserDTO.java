package com.project.telegram_bot.model.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class UserDTO {
    private Long chatID;
    private String userName;
    private String firstName;
    private String lastName;
    private Timestamp subscribedAt;
    private int level;
    private String isActive;
    private int freeLesson;
}

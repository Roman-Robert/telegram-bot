package com.project.telegram_bot.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity(name = "usersDataTable")
@Data
public class User {

    @Id
    private Long chatID;
    private String userName;
    private String firstName;
    private String lastName;
    private Timestamp subscribedAt;
    private int level;
    private String isActive;

}

package com.project.telegram_bot.mapper;

import com.project.telegram_bot.model.dto.UserDTO;
import com.project.telegram_bot.model.entity.UserEntity;

public class UserMapper {

    public UserDTO entityToDto(UserEntity userEntity) {
        return UserDTO.builder()
                .chatID(userEntity.getChatID())
                .userName(userEntity.getUserName())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .subscribedAt(userEntity.getSubscribedAt())
                .level(userEntity.getLevel())
                .isActive(userEntity.getIsActive())
                .freeLesson(userEntity.getFreeLesson())
                .build();
    }

    public UserEntity dtoToEntity(UserDTO userDTO) {
        return UserEntity.builder()
                .chatID(userDTO.getChatID())
                .userName(userDTO.getUserName())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .subscribedAt(userDTO.getSubscribedAt())
                .level(userDTO.getLevel())
                .isActive(userDTO.getIsActive())
                .freeLesson(userDTO.getFreeLesson())
                .build();
    }
}

package com.project.telegram_bot.mapper;

import com.project.telegram_bot.model.dto.UserDTO;
import com.project.telegram_bot.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO entityToDto(UserEntity userEntity) {
        return UserDTO.builder()
                .chatId(userEntity.getChatId())
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
                .chatId(userDTO.getChatId())
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

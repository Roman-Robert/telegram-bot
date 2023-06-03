package com.project.telegram_bot.service;

import com.project.telegram_bot.mapper.UserMapper;
import com.project.telegram_bot.model.dto.UserDTO;
import com.project.telegram_bot.model.entity.UserEntity;
import com.project.telegram_bot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    public void saveUser(UserDTO user) {
        userRepository.save(userMapper.dtoToEntity(user));
    }

    public List<UserDTO> getAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities
                .stream()
                .map(userMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(long id) {
        UserEntity user = userRepository.findById(id);
        return userMapper.entityToDto(user);
    }

    public void deleteUser(UserDTO user) {
        user.setIsActive("NO");
        userRepository.save(userMapper.dtoToEntity(user));
    }
}

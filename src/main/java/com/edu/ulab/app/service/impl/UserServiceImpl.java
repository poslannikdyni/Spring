package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dao.impl.inmemory.UserDao;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.exception.DaoException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.utility.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;

    public UserServiceImpl(UserDao userDao, UserMapper userMapper) {
        this.userDao = userDao;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setId(IdGenerator.nextUserId());
        UserEntity userEntity = userMapper.userDtoToUserEntity(userDto);
        try {
            userEntity = userDao.create(userEntity);
        } catch (DaoException e) {
            throw e;
        }
        return userMapper.userEntityToUserDto(userEntity);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserEntity userEntity = userMapper.userDtoToUserEntity(userDto);
        try {
            userEntity = userDao.update(userDto.getId(), userEntity);
        } catch (DaoException e) {
            throw e;
        }
        return userMapper.userEntityToUserDto(userEntity);
    }

    @Override
    public UserDto getUserById(Long id) {
        try {
            UserEntity userEntity = userDao.getById(id);
            return userMapper.userEntityToUserDto(userEntity);
        } catch (DaoException e) {
            throw e;
        }
    }

    @Override
    public void deleteUserById(Long id) {
        try {
            userDao.delete(id);
        } catch (DaoException e) {
            throw e;
        }
    }
}

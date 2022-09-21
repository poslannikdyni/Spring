package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dao.impl.inmemory.UserDao;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.utility.ExceptionUtility;
import com.edu.ulab.app.utility.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;
    private final ExceptionUtility exceptionUtility;

    public UserServiceImpl(UserDao userDao, UserMapper userMapper, ExceptionUtility exceptionUtility) {
        this.userDao = userDao;
        this.userMapper = userMapper;
        this.exceptionUtility = exceptionUtility;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        exceptionUtility.throwServiceExceptionIfNull(userDto, "Create user failed : userDto is null");

        userDto.setId(IdGenerator.nextUserId());
        UserEntity userEntity = userMapper.userDtoToUserEntity(userDto);
        userEntity = userDao.create(userEntity);

        exceptionUtility.throwServiceExceptionIfNull(userEntity, "Create user failed.");
        log.info("Create user successfully {}", userEntity);
        return userMapper.userEntityToUserDto(userEntity);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        exceptionUtility.throwServiceExceptionIfNull(userDto, "Update user failed : userDto is null");
        exceptionUtility.throwServiceExceptionIfNull(userDto.getId(), "Update user failed : userDto.getId() returns null");

        UserEntity userEntity = userMapper.userDtoToUserEntity(userDto);
        userEntity = userDao.update(userDto.getId(), userEntity);

        exceptionUtility.throwNotFoundExceptionIfNull(userEntity, String.format("Update user failed : user with id = %s not exist", userDto.getId()));
        log.info("Update user successfully {}", userEntity);
        return userMapper.userEntityToUserDto(userEntity);
    }

    @Override
    public UserDto getUserById(Long id) {
        exceptionUtility.throwServiceExceptionIfNull(id, "Get user failed : id is null");
        UserEntity userEntity = userDao.getById(id);
        exceptionUtility.throwNotFoundExceptionIfNull(userEntity, String.format("Get user failed : user with id = %s not exist", id));
        log.info("Get user successfully {}", userEntity);
        return userMapper.userEntityToUserDto(userEntity);
    }

    @Override
    public void deleteUserById(Long id) {
        exceptionUtility.throwServiceExceptionIfNull(id, "Delete user failed : id is null");
        UserEntity userEntity = userDao.delete(id);
        exceptionUtility.throwNotFoundExceptionIfNull(userEntity, String.format("Delete user failed : user with id = %s not exist", id));
        log.info("Delete user successfully {}", userEntity);
    }

}

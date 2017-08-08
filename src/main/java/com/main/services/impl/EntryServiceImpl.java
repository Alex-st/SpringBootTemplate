package com.main.services.impl;

import com.main.exceptions.UserRuntimeException;
import com.main.model.UserModel;
import com.main.persistence.entities.UserEntity;
import com.main.persistence.repositories.UserRepository;
import com.main.services.EntryService;
import com.main.utils.UserEntityConvertor;
import org.omg.CORBA.UserException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Oleksandr on 10/6/2016.
 */
@Service
public class EntryServiceImpl implements EntryService {

    @Inject
    private UserRepository userRepository;

    @Override
    public UserModel getUserModelByUsername(String username) {

        UserEntity userEntity = userRepository.findOneByLogin(username);
        if (userEntity == null) {
            throw new UserRuntimeException("User " + username + " not found!");
        }
        return UserEntityConvertor.convertToModel(userEntity);
    }

    @Override
    public void saveUser(UserModel userModel) {
        UserEntity userEntity = UserEntityConvertor.convertToEntity(userModel);
        userRepository.save(userEntity);
    }

    @Override
    public List<UserModel> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream()
                .map(UserEntityConvertor::convertToModel)
                .collect(Collectors.toList());
    }
}

package com.main.utils;

import com.main.model.UserModel;
import com.main.persistence.entities.UserEntity;

/**
 * Created by Oleksandr on 10/10/2016.
 */
public class UserEntityConvertor {

    public static UserModel convertToModel(UserEntity userEntity) {
        UserModel userModel = new UserModel();
        userModel.setEnabled(userEntity.getEnabled());
        userModel.setUsername(userEntity.getLogin());
        userModel.setPassword(userEntity.getPassword());

        return userModel;
    }

    public static UserEntity convertToEntity(UserModel userModel) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEnabled(userModel.isEnabled());
        userEntity.setLogin(userModel.getUsername());
        userEntity.setPassword(userModel.getPassword());

        return userEntity;
    }
}

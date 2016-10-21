package com.main.services;

import com.main.model.UserModel;

import java.util.List;

/**
 * Created by Oleksandr on 10/6/2016.
 */
public interface EntryService {

    UserModel getUserModelByUsername(String username);

    List<UserModel> getAllUsers();

    void saveUser(UserModel userModel);
}

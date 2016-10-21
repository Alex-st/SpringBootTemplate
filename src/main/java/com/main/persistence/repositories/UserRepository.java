package com.main.persistence.repositories;

import com.main.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Oleksandr on 10/10/2016.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findAllByEnabledIsTrue();

    UserEntity findOneByLogin(String username);
}

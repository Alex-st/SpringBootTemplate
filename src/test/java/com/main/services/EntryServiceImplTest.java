package com.main.services;

import com.main.exceptions.UserRuntimeException;
import com.main.model.UserModel;
import com.main.persistence.entities.UserEntity;
import com.main.persistence.repositories.UserRepository;
import com.main.services.impl.EntryServiceImpl;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Oleksandr on 10/17/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class EntryServiceImplTest {

    @InjectMocks
    private EntryServiceImpl sut = new EntryServiceImpl();

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityCaptor;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void shouldSuccessfullyFindUser() {
        when(userRepository.findOneByLogin(any(String.class))).thenReturn(constructUserEntity("test"));
        UserModel model = sut.getUserModelByUsername("test");

        verify(userRepository, times(1)).findOneByLogin(eq("test"));
        assertThat(model.getPassword(), is("password"));
    }

    @Test(expected = UserRuntimeException.class)
    public void shouldThrowUserRuntimeExceptionIfUserLoginNotFound() {
        when(userRepository.findOneByLogin(any(String.class))).thenReturn(null);
        sut.getUserModelByUsername("test");
    }

    @Test
    public void shouldThrowUserRuntimeExceptionUsingRule() {

        expectedEx.expect(UserRuntimeException.class);
        expectedEx.expectMessage("User test not found!");

        when(userRepository.findOneByLogin(any(String.class))).thenReturn(null);
        sut.getUserModelByUsername("test");
    }

    @Test
    public void shouldSuccessfullySaveUser() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(constructUserEntity("test"));
        sut.saveUser(constructUserModel("test"));

        verify(userRepository, never()).findOneByLogin(eq("test"));
        verify(userRepository, times(1)).save(userEntityCaptor.capture());

        assertThat(userEntityCaptor.getValue().getPassword(), Matchers.is("password"));
    }

    private UserModel constructUserModel(String login) {
        UserModel model = new UserModel();
        model.setEnabled(true);
        model.setPassword("password");
        model.setUsername(login);
        return model;
    }

    private UserEntity constructUserEntity(String login) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEnabled(true);
        userEntity.setId(1L);
        userEntity.setLogin(login);
        userEntity.setPassword("password");
        return userEntity;
    }
}

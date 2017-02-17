package com.main.repositories;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.main.Application;
import com.main.persistence.entities.UserEntity;
import com.main.persistence.repositories.UserRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Oleksandr on 10/11/2016.
 */
@Transactional
@RunWith(SpringRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@SpringBootTest(classes = Application.class)
public class UserRepositoryTest {

    static final String DATASET = "com/main/repositories/UserRepositoryTest_data.xml";

    @Inject
    private UserRepository sut;

    @Test
    @Ignore
    @DatabaseSetup(value = DATASET, type = DatabaseOperation.INSERT)
    @DatabaseTearDown(value = DATASET, type = DatabaseOperation.DELETE)
    public void shouldFindAllActiveUsers() {

        List<UserEntity> entities = sut.findAll();

        //then
        assertThat(entities, is(notNullValue()));
        assertThat(entities, is(not(emptyCollectionOf(UserEntity.class))));
        assertThat(entities.size(), is(2));
//        assertThat(entities, hasItem());

        assertEquals("test1", entities.get(0).getLogin());
        assertEquals("test2", entities.get(1).getLogin());
    }
}

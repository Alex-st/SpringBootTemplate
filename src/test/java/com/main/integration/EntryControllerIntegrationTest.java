package com.main.integration;

import com.main.model.UserModel;
import com.main.persistence.entities.UserEntity;
import com.main.persistence.repositories.UserRepository;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
//todo can be added proper @Sql("/init-for-full-integration-test.sql") instead of userRepository mock
public class EntryControllerIntegrationTest {

    @Autowired
    private TestRestTemplate template;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void successfullyFetchUsers() {
        UserEntity userEntity = createUserEntity();

        when(userRepository.findAll()).thenReturn(Arrays.asList(userEntity));

        ResponseEntity<UserModel[]> response = template.getForEntity("/dev/user", UserModel[].class);

        assertThat(response.getStatusCode().value(), is(HttpStatus.SC_OK));
        assertThat(response.getHeaders().getContentType(), is(MediaType.APPLICATION_JSON_UTF8));
        assertThat(response.getBody()[0].toString(), containsString("TestLogin"));

    }

    @Test
    @Ignore
    public void failWith404WhenUserNotFound() {

        when(userRepository.findOneByLogin(any(String.class))).thenReturn(null);

        ResponseEntity<UserModel> response = template.getForEntity("/dev/user/test", UserModel.class);

        assertThat(response.getStatusCode().value(), is(HttpStatus.SC_NOT_FOUND));
        assertThat(response.getHeaders().getContentType(), is(MediaType.APPLICATION_JSON_UTF8));

    }

    private UserEntity createUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(100L);
        userEntity.setEnabled(true);
        userEntity.setLogin("TestLogin");
        userEntity.setPassword("TestPassword");
        return userEntity;
    }

}

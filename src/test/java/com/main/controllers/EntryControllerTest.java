package com.main.controllers;

import com.main.exceptions.UserRuntimeException;
import com.main.model.UserModel;
import com.main.services.EntryService;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by Oleksandr on 10/17/2016.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(EntryController.class)
public class EntryControllerTest {

    static final String PATH_API = "/dev/user";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EntryService userService;

    private HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

    @Test
    public void shouldReturnAllUsersSuccess() throws Exception {
        when(userService.getAllUsers()).thenReturn(asList(createUserModel()));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(PATH_API).content("application/json")).andReturn();

        int status = result.getResponse().getStatus();
        String contentAsString = result.getResponse().getContentAsString();
        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
        assertEquals(HttpStatus.SC_OK, status);
        assertEquals("[{\"username\":\"userSuccess@test.com\",\"password\":\"password\",\"enabled\":true}]", contentAsString);
    }

    @Test
    public void shouldSuccessfullySaveUser() throws Exception {
        UserModel userModel = createUserModel();

        String json = objectToJsonString(userModel);
        MvcResult result = mockMvc.perform(
                post(PATH_API).content(json)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = result.getResponse().getStatus();
        verify(userService, times(1)).saveUser(any(UserModel.class));
        verifyNoMoreInteractions(userService);
        assertEquals(HttpStatus.SC_OK, status);
    }

    @Test
    public void shouldNotFindUser() throws Exception {
        when(userService.getAllUsers()).thenThrow(new UserRuntimeException("User userSuccess@test.com not found!"));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(PATH_API).content("application/json")).andReturn();

        int status = result.getResponse().getStatus();
        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
        assertEquals(HttpStatus.SC_NOT_FOUND, status);
    }

    private UserModel createUserModel() {
        UserModel testModel = new UserModel();
        testModel.setUsername("userSuccess@test.com");
        testModel.setPassword("password");
        testModel.setEnabled(true);
        return testModel;
    }

    private String objectToJsonString(final Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}

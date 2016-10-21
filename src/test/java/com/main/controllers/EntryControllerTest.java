package com.main.controllers;

import com.main.model.UserModel;
import com.main.services.EntryService;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
@RunWith(SpringJUnit4ClassRunner.class)
public class EntryControllerTest extends AbstractControllerTest {

    static final String PATH_API = "/sbp/dev/user";

    @InjectMocks
    private EntryController entryController;

    @Mock
    private EntryService userService;

    private HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.initBaseController(entryController);
    }

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
        assertEquals("[{\"status\":\"SUCCESS\",\"statusMessage\":null,\"username\":\"Test@test.com\"}]", contentAsString);
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

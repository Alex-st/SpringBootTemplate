package com.main.controllers;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Created by Oleksandr on 10/19/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
public abstract class AbstractControllerTest {

    protected MockMvc mockMvc;

    /*
    There are two ways of initialization spring context for mock mvc:
    1. MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    2. MockMvcBuilders.standaloneSetup(controller).build()
    */
    protected void initBaseController(Object controller) {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
}
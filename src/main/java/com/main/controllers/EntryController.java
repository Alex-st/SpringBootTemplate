package com.main.controllers;

import com.main.exceptions.UserRuntimeException;
import com.main.model.UserModel;
import com.main.services.EntryService;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by Oleksandr on 10/6/2016.
 */
@RestController
@RequestMapping("/dev")
public class EntryController {

    private static final Logger LOG = LoggerFactory.getLogger(EntryController.class);

    @Inject
    private EntryService entryService;

    /**
     * @return Returns single {@link UserModel}
     * @description Search for user by its username.
     * @httpMethod GET
     * @httpUrl http://{host}:{port}/sbp/dev/user/:username
     * @httpUrlExample http://localhost:8080/sbp/dev/user/:username
     * @returnType application/json
     */
    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    public ResponseEntity<UserModel> getUserModel(final @PathVariable("username") String username) {
        return new ResponseEntity<>(entryService.getUserModelByUsername(username), HttpStatus.OK);
    }

    /**
     * @return Returns list of {@link UserModel}
     * @description Return all users.
     * @httpMethod GET
     * @httpUrl http://{host}:{port}/sbp/dev/user
     * @httpUrlExample http://localhost:8080/sbp/dev/user
     * @returnType application/json
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<List<UserModel>> getAllUsers() {
        LOG.info("GetAllUsers Controller entry point");
        return new ResponseEntity<>(entryService.getAllUsers(), HttpStatus.OK);
    }

    /**
     * @description Method to create new user
     * @httpMethod POST
     * @httpUrl http://{host}:{port}/sbp/dev/user
     * @httpUrlExample http://localhost:8080/sbp/dev/user
     * @requestBodyExample {"username" : "Maya007", "password" : "password", "enabled" : false}
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<Void> saveUser(@RequestBody @Valid final UserModel userModel) {
        entryService.saveUser(userModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @description Method to change log level, logName could have only lowercase letters name
     * @httpMethod POST
     * @httpUrl http://{host}:{port}/sbp/dev/log/:logLevel/:logName
     * @httpUrlExample http://localhost:8080/sbp/dev/log/DEBUG/mylogger}
     */
    @RequestMapping(value = "/log/{logLevel}/{logName:[a-z-]+}", method = RequestMethod.PUT)
    public ResponseEntity<String> setLogLevel(final @PathVariable("logLevel") String logLevel, final @PathVariable("logName") String logName) {
        LOG.warn("About set logger {} to {} level", logName, logLevel);

        org.apache.log4j.Logger logger = LogManager.getLogger(logName);

        Level prevLevel = logger.getLevel();
        logger.setLevel(Level.toLevel(logLevel));

        return new ResponseEntity<>("Set logger " + logger.getName() + " from " + prevLevel + " to " + logger.getLevel(), HttpStatus.OK);
    }

    @ExceptionHandler({UserRuntimeException.class})
    ResponseEntity<String> handleNotFoundRequests(Exception ex) throws IOException {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}

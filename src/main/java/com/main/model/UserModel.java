package com.main.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Oleksandr on 10/10/2016.
 */
@Getter
@Setter
@ToString
public class UserModel {

    public static final String USERNAME_NOT_NULL = "Username shouldn't be null";
    public static final String PASSWORD_NOT_NULL = "Password shouldn't be null";
    public static final String DATA_NOT_VALID = "Input data violates length restriction";

    @NotNull(message = USERNAME_NOT_NULL)
    @Size(min=6, max=50, message = DATA_NOT_VALID)
    @JsonProperty("username")
    private String username;

    @NotNull(message = PASSWORD_NOT_NULL)
    @Size(min=6, max=50, message = DATA_NOT_VALID)
    @JsonProperty("password")
    private String password;

    @JsonProperty("enabled")
    boolean enabled;
}

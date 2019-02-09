package com.netas.cpaas.user.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginDto implements Serializable {

    private String username;

    private String password;
}

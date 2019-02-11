package com.netas.cpaas.user.model.register;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.netas.cpaas.user.model.ServiceAddress;
import com.netas.cpaas.user.model.UserAddress;
import com.netas.cpaas.user.validation.Password;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "email",
        "firstName",
        "lastName",
        "password",
        "serviceAddress",
        "userAddress",
        "userName"
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto implements Serializable {

    @JsonProperty("email")
    @Email
    private String email;

    @JsonProperty("firstName")
    @NotBlank(message = "{user.firstName.blank}" )
    private String firstName;

    @NotBlank(message = "{user.firstName.blank}" )
    @JsonProperty("lastName")
    private String lastName;

    @Password
    @JsonProperty("password")
    private String password;

    @JsonProperty("serviceAddress")
    private ServiceAddress serviceAddress;

    @JsonProperty("userAddress")
    private UserAddress userAddress;

    @JsonProperty("userName")
    @NotBlank
    private String userName;

}

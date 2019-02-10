package com.netas.cpaas.user.model.create;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class CreatedUserDto implements Serializable {

    @JsonProperty("data")
    private DataOfCreatedUser dataOfCreatedUser;
}

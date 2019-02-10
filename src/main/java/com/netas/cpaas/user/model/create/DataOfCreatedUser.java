package com.netas.cpaas.user.model.create;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.netas.cpaas.user.model.NvsUser;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class DataOfCreatedUser implements Serializable {

    @JsonProperty("createUser")
    private NvsUser nvsUser;
}

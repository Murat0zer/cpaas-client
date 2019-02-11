package com.netas.cpaas.user.model.nvs.create;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class NvsCreatedUser implements Serializable {

    @JsonProperty("data")
    private DataOfCreatedNvsUser dataOfCreatedNvsUser;
}

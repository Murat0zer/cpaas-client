package com.netas.cpaas.user.model.nvs.create;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.netas.cpaas.user.model.NvsUserInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class DataOfCreatedNvsUser implements Serializable {

    @JsonProperty("createUser")
    private NvsUserInfo nvsUserInfo;
}

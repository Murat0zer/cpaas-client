package com.netas.cpaas.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NvsLoginDto implements Serializable {

    @JsonProperty("username")
    private String email;

    private String password;

    @JsonProperty("grant_type")
    private String grantType;

    @JsonProperty("client_id")
    private String clientId;

    private String scope;

}

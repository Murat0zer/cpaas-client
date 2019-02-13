package com.netas.cpaas.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NvsUserInfo implements Serializable {

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("preferred_username")
    private String preferredUsername;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("services-identity")
    private String servicesIdentity;

    @JsonProperty("sms-did-list")
    private Set<String> smsDidList;

    @JsonProperty("call-did-list")
    private Set<String> callDidList;
}


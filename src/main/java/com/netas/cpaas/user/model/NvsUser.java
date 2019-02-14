package com.netas.cpaas.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class NvsUser implements Serializable {

    @JsonProperty("sub")
    private String sub;

    @Id
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

    @Transient
    @JsonProperty("sms-did-list")
    private Set<String> smsDidList;

    @Transient
    @JsonProperty("call-did-list")
    private Set<String> callDidList;

    @JsonIgnore
    @Transient
    private NvsUserInfo nvsUserInfo;

    @JsonIgnore
    @Transient
    private NvsTokenInfo nvsTokenInfo;

}


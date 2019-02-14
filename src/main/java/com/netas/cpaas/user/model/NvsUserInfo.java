package com.netas.cpaas.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NvsUserInfo implements Serializable {

    @JsonProperty("__typename")
    private String typename;

    @JsonProperty("id")
    private String nvsId;

    @JsonProperty("createdOn")
    private String createdOn;

    @JsonProperty("status")
    private String status;

    @Transient
    @Builder.Default
    @JsonProperty("roles")
    private Set<Role> nvsRoles = new HashSet<>();

}

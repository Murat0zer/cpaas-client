package com.netas.cpaas.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Embeddable;
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
@Entity
public class NvsUser implements Serializable {

    @Transient
    private NvsTokenInfo nvsTokenInfo;

    @JsonProperty("__typename")
    private String typename;

    @Id
    @JsonProperty("id")
    private String nvsId;

    @JsonProperty("createdOn")
    private String createdOn;

    @JsonProperty("status")
    private String status;

    @Builder.Default
    @JsonProperty("roles")
    private Set<String> nvsRoles = new HashSet<>();
}

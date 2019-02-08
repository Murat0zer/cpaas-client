package com.netas.cpaas.user.model;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Transient;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class NvsUser {

    @Transient
    private NvsTokens nvsTokens;

    private String nvsUsername;

    private String nvsEmail;

    private String nvsPassword;
}

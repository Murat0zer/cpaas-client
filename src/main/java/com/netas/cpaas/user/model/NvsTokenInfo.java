package com.netas.cpaas.user.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NvsTokenInfo implements Serializable {

    private String accessToken;
    private long expiresIn;
    private long refreshExpiresIn;
    private String refreshToken;
    private String tokenType;
    private String idToken;
    private long notBeforePolicy;
    private String sessionState;
    private String scope;
}

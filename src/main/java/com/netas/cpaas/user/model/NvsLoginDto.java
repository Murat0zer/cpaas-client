package com.netas.cpaas.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NvsLoginDto {

    private String username;

    private String password;

    private String grantType;

    private String clientId;

    private String scope;

}

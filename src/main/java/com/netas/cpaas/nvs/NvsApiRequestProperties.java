package com.netas.cpaas.nvs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NvsApiRequestProperties {

    private String baseUrl;

    private String apiName;

    private String apiVersion;

    private String userId;
}

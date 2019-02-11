package com.netas.cpaas.user.model.nvs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.netas.cpaas.user.model.register.Variables;
import lombok.*;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "operationName",
        "query",
        "variables"
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NvsGraphqlDto implements Serializable {

    @JsonProperty("operationName")
    public String operationName;

    @JsonProperty("query")
    public String query;

    @JsonProperty("variables")
    public Variables variables;

}

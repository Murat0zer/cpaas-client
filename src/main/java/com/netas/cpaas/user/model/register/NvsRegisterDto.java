
package com.netas.cpaas.user.model.register;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
public class NvsRegisterDto implements Serializable {

    @JsonProperty("operationName")
    public String operationName;

    @JsonProperty("query")
    public String query;

    @JsonProperty("variables")
    public Variables variables;

    @JsonIgnore
    private String xToken;

}

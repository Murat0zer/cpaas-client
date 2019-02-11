
package com.netas.cpaas.user.model.register;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "input"
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Variables<T> implements Serializable {

    @JsonProperty("input")
    public T input;

}

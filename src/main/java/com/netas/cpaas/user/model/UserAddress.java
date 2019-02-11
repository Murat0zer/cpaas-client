package com.netas.cpaas.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "addressLine1",
        "addressLine2",
        "city",
        "country",
        "postalCode",
        "state"
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserAddress implements Serializable {

    @JsonProperty("addressLine1")
    public String addressLine1;
    @JsonProperty("addressLine2")
    public String addressLine2;
    @JsonProperty("city")
    public String city;
    @JsonProperty("country")
    public String country;
    @JsonProperty("postalCode")
    public String postalCode;
    @JsonProperty("state")
    public String state;

}

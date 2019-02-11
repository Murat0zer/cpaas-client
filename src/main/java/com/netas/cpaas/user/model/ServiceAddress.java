
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
public class ServiceAddress implements Serializable {

    @JsonProperty("addressLine1")
    public String serviceAddressLine1;
    @JsonProperty("addressLine2")
    public String serviceAddressLine2;
    @JsonProperty("city")
    public String serviceCity;
    @JsonProperty("country")
    public String serviceCountry;
    @JsonProperty("postalCode")
    public String servicePostalCode;
    @JsonProperty("state")
    public String serviceState;

}

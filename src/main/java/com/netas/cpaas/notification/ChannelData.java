
package com.netas.cpaas.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "x-webhookURL",
    "x-authorization"
})

@Builder
public class ChannelData implements Serializable {

    @JsonProperty("x-webhookURL")
    public String xWebhookURL;

    @JsonProperty("x-authorization")
    public String xAuthorization;
}

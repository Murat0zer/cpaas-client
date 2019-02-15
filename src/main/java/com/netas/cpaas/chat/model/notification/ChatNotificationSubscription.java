
package com.netas.cpaas.chat.model.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

@JsonPropertyOrder({
    "callbackReference",
    "clientCorrelator",
    "resourceURL"
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatNotificationSubscription implements Serializable
{

    @JsonProperty("callbackReference")
    private CallbackReference callbackReference;
    @JsonProperty("clientCorrelator")
    private String clientCorrelator;
    @JsonProperty("resourceURL")
    private String resourceURL;

}

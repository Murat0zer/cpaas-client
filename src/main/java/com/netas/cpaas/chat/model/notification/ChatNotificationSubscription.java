
package com.netas.cpaas.chat.model.notification;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "callbackReference",
    "clientCorrelator",
    "resourceURL"
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotificationSubscription implements Serializable
{

    @JsonProperty("callbackReference")
    public CallbackReference callbackReference;
    @JsonProperty("clientCorrelator")
    public String clientCorrelator;
    @JsonProperty("resourceURL")
    public String resourceURL;

}
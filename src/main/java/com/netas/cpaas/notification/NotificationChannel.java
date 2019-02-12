
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
    "callbackURL",
    "channelData",
    "channelType",
    "clientCorrelator",
    "resourceURL"
})

@Builder
public class NotificationChannel implements Serializable {

    @JsonProperty("callbackURL")
    public String callbackURL;

    @JsonProperty("channelData")
    public ChannelData channelData;

    @JsonProperty("channelType")
    public String channelType;

    @JsonProperty("clientCorrelator")
    public String clientCorrelator;

    @JsonProperty("resourceURL")
    public String resourceURL;

}

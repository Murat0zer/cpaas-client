
package com.netas.cpaas.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationChannel implements Serializable {

    @JsonProperty("callbackURL")
    public String callbackURL;

    @JsonProperty("channelData")
    public ChannelData channelData;

    @JsonProperty("channelLifetime")
    public Integer channelLifetime;

    @JsonProperty("channelType")
    public String channelType;

    @JsonProperty("clientCorrelator")
    public String clientCorrelator;

    @JsonProperty("x-connCheckRole")
    public String xConnCheckRole;

    @JsonProperty("resourceURL")
    public String resourceURL;

}

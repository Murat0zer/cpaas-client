
package com.netas.cpaas.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationChannel implements Serializable {

    @JsonProperty("callbackURL")
    private String callbackURL;

    @JsonProperty("channelData")
    private ChannelData channelData;

    @JsonProperty("channelLifetime")
    private Integer channelLifetime;

    @JsonProperty("channelType")
    private String channelType;

    @JsonProperty("clientCorrelator")
    private String clientCorrelator;

//    @JsonProperty("x-connCheckRole")
//    private String xConnCheckRole;

    @JsonProperty("resourceURL")
    private String resourceURL;

}

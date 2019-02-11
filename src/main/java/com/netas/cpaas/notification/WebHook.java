
package com.netas.cpaas.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.netas.cpaas.notification.NotificationChannel;
import lombok.*;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "notificationChannel"
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebHook implements Serializable {

    @JsonProperty("notificationChannel")
    public NotificationChannel notificationChannel;

}

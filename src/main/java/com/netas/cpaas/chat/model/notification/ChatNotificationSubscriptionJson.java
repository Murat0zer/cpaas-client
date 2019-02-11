
package com.netas.cpaas.chat.model.notification;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "chatNotificationSubscription"
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotificationSubscriptionJson implements Serializable
{

    @JsonProperty("chatNotificationSubscription")
    private ChatNotificationSubscription chatNotificationSubscription;

}

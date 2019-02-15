
package com.netas.cpaas.chat.model.chatmessagenotification;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({
    "chatMessageNotification"
})
public class ChatMessageNotificationJson implements Serializable
{

    @JsonProperty("chatMessageNotification")
    private ChatMessageNotification chatMessageNotification;

}


package com.netas.cpaas.chat.model.chatmessagenotification;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.netas.cpaas.chat.model.message.ChatMessage;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({
    "link",
    "chatMessage",
    "dateTime",
    "id"
})
public class ChatMessageNotification implements Serializable
{

    @JsonProperty("link")
    private List<Link> link = null;
    @JsonProperty("chatMessage")
    private ChatMessage chatMessage;
    @JsonProperty("dateTime")
    private Integer dateTime;
    @JsonProperty("id")
    private String id;

}


package com.netas.cpaas.chat.model.message;

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
    "text",
    "x-type",
    "dateTime",
    "status",
    "senderAddress",
    "x-destinationAddress"
})
public class ChatMessage implements Serializable
{

    @JsonProperty("text")
    private String text;
    @JsonProperty("x-type")
    private String xType;
    @JsonProperty("dateTime")
    private Integer dateTime;
    @JsonProperty("status")
    private String status;
    @JsonProperty("senderAddress")
    private String senderAddress;
    @JsonProperty("x-destinationAddress")
    private String xDestinationAddress;

}

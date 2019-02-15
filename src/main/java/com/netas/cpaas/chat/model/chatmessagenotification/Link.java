
package com.netas.cpaas.chat.model.chatmessagenotification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({
        "href",
        "rel"
})
public class Link implements Serializable
{

    @JsonProperty("href")
    private String href;
    @JsonProperty("rel")
    private String rel;

}

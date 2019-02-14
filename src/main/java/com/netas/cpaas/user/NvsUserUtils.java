package com.netas.cpaas.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netas.cpaas.user.model.NvsUser;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

@Slf4j
public class NvsUserUtils {

    private NvsUserUtils() {

    }
    public static NvsUser getNvsUserFromIdToken(String idToken) {

        NvsUser nvsUser = NvsUser.builder().build();
        Base64.Decoder decoder = Base64.getDecoder();
        ObjectMapper objectMapper = new ObjectMapper();
        String decodedValue = new String(decoder.decode(idToken.split("\\.")[1]));

        try {
            nvsUser = objectMapper.readValue(decodedValue, NvsUser.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return nvsUser;
    }
}

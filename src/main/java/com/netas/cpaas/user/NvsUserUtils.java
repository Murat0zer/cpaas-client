package com.netas.cpaas.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netas.cpaas.user.model.NvsUserInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

@Slf4j
public class NvsUserUtils {

    public static NvsUserInfo getNvsUserInfoFromIdToken(String idToken) {

        NvsUserInfo nvsUserInfo = new NvsUserInfo();
        Base64.Decoder decoder = Base64.getDecoder();
        ObjectMapper objectMapper = new ObjectMapper();
        String decodedValue = new String(decoder.decode(idToken.split("\\.")[1]));

        try {
            nvsUserInfo = objectMapper.readValue(decodedValue, NvsUserInfo.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return nvsUserInfo;
    }
}

package com.netas.cpaas.nvs;

import lombok.Builder;
import lombok.Data;

public class NvsApiRequestUrl {

    private static String apiName;
    private static String apiVersion;
    private static String userId;

    private NvsApiRequestUrl() {
    }

    public static String getUrlForApiRequest() {
        String apiBaseUrl = "https://nvs-cpaas-oauth.kandy.io/cpaas";
        return apiBaseUrl + "/" + apiName + "/" + apiVersion + "/" + userId;
    }

    public static String getUrlForWebSocket() {

        String websocketBaseUrl = "wss://nvs-cpaas-oauth.kandy.io/cpaas";
        return websocketBaseUrl + "/" + apiName + "/" + apiVersion + "/" + userId;

    }

    public static void configureApiRequest(NvsApiRequestProperties nvsApiRequestProperties) {

        NvsApiRequestUrl.apiName = nvsApiRequestProperties.getApiName();
        NvsApiRequestUrl.apiVersion = nvsApiRequestProperties.getApiVersion();
        NvsApiRequestUrl.userId = nvsApiRequestProperties.getUserId();

    }

    public static String getApiName() {
        return apiName;
    }

    public static void setApiName(String apiName) {
        NvsApiRequestUrl.apiName = apiName;
    }

    public static String getApiVersion() {
        return apiVersion;
    }

    public static void setApiVersion(String apiVersion) {
        NvsApiRequestUrl.apiVersion = apiVersion;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        NvsApiRequestUrl.userId = userId;
    }



}

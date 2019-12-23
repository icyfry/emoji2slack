package io.icyfry.bitbucket.e2s.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Global configuration of the plugin
 */
public class GlobalConfiguration {

    // Slack bot token
    @JsonProperty
    private String botAccessToken;
    
    public GlobalConfiguration() {
    }

    public String getBotAccessToken() {
        return botAccessToken;
    }

    public void setBotAccessToken(String botAccessToken) {
        this.botAccessToken = botAccessToken;
    }

}
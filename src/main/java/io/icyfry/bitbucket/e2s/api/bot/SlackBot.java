package io.icyfry.bitbucket.e2s.api.bot;

import com.hubspot.slack.client.models.response.chat.ChatPostMessageResponse;

/**
 * Slack Bot of the plugin
 */
public interface SlackBot {

    /**
     * Stop the Bot
     * @throws Emoji2SlackBotException
     */
    public void stop() throws SlackBotException;
    
    /**
     * Send a message to Slack on a specified channel
     * @param channelId the id of the Slack channel 
     * @return Slack response
     * @throws Emoji2SlackBotException
     */
    public ChatPostMessageResponse sendMessage(String channelId) throws SlackBotException;

}
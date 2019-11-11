package io.icyfry.bitbucket.e2s.api.bot;

import com.atlassian.bitbucket.comment.Comment;
import com.hubspot.slack.client.models.response.chat.ChatPostMessageResponse;

import io.icyfry.bitbucket.e2s.api.EmojiConfiguration;

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
     * @param message the message to send
     * @return Slack response
     * @throws Emoji2SlackBotException
     */
    public ChatPostMessageResponse sendMessage(String channelId, String message) throws SlackBotException;

    /**
     * Send a message to slack regarding a configuation
     * @param emojiConfiguration the configuration
     * @return Slack response
     * @throws SlackBotException
     */
    public ChatPostMessageResponse sendConfigurationMessage(EmojiConfiguration emojiConfiguration) throws SlackBotException;

    /**
     * Send a comment to slack
     * @param channelId the id of the Slack channel
     * @param comment the comment to send
     * @return Slack response
     * @throws SlackBotException
     */
    public ChatPostMessageResponse sendCommentMessage(String channelId, Comment comment) throws SlackBotException;

}
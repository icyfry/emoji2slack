package io.icyfry.bitbucket.e2s.impl;

import java.io.IOException;

import com.atlassian.bitbucket.comment.Comment;
import com.hubspot.algebra.Result;
import com.hubspot.slack.client.SlackClient;
import com.hubspot.slack.client.SlackClientRuntimeConfig;
import com.hubspot.slack.client.SlackWebClient;
import com.hubspot.slack.client.methods.params.chat.ChatPostMessageParams;
import com.hubspot.slack.client.models.response.SlackError;
import com.hubspot.slack.client.models.response.chat.ChatPostMessageResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.icyfry.bitbucket.e2s.api.EmojiConfiguration;
import io.icyfry.bitbucket.e2s.api.bot.SlackBot;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotException;

public class SlackBotImpl implements SlackBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackBotImpl.class);

    private final SlackClient slackClient;

    public SlackBotImpl(SlackWebClient.Factory clientFactory, String slackToken) throws SlackBotException {
        try {
            this.slackClient = clientFactory.build(SlackClientRuntimeConfig.builder().setTokenSupplier(() -> slackToken)
                    // ... all your configuration here
                    .build());
        } catch (IllegalStateException e) {
            throw new SlackBotException(e);
        }
    }

    @Override
    public void stop() throws SlackBotException {
        try {
            slackClient.close();
        } catch (IOException e) {
            throw new SlackBotException(e);
        }
    }

    @Override
    public ChatPostMessageResponse sendMessage(String channelId, String message) throws SlackBotException {
        try {
            Result<ChatPostMessageResponse, SlackError> postResult = slackClient
                    .postMessage(ChatPostMessageParams.builder().setText(message).setChannelId(channelId).build())
                    .join();
            return postResult.unwrapOrElseThrow();
        } catch (IllegalStateException e) {
            throw new SlackBotException(e);
        }
    }

    @Override
    public ChatPostMessageResponse sendConfigurationMessage(EmojiConfiguration configuration) throws SlackBotException {
        return this.sendMessage(configuration.getChannelId(),
                "your channel is now linked to the emoji : " + configuration.getEmoji().getValue().get());
    }

    @Override
    public ChatPostMessageResponse sendCommentMessage(String channelId, Comment comment) throws SlackBotException {
        return this.sendMessage(channelId, comment.getText());
    }

}
package io.icyfry.bitbucket.e2s.impl;

import java.io.IOException;

import com.hubspot.algebra.Result;
import com.hubspot.slack.client.SlackClient;
import com.hubspot.slack.client.SlackClientRuntimeConfig;
import com.hubspot.slack.client.SlackWebClient;
import com.hubspot.slack.client.methods.params.chat.ChatPostMessageParams;
import com.hubspot.slack.client.models.response.SlackError;
import com.hubspot.slack.client.models.response.chat.ChatPostMessageResponse;

import io.icyfry.bitbucket.e2s.api.bot.SlackBot;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotException;

public class SlackBotImpl implements SlackBot {

    private final SlackClient slackClient;
    
    private final String slackToken;
  
    public SlackBotImpl (SlackWebClient.Factory clientFactory) throws SlackBotException {

        slackToken = ""; // TODO read token

        try{

        this.slackClient = clientFactory.build(
            SlackClientRuntimeConfig.builder()
                .setTokenSupplier(() -> slackToken)
                // ... all your configuration here
                .build()
        );

        }
        catch(IllegalStateException e){
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
    public ChatPostMessageResponse sendMessage(String channelId) throws SlackBotException {
       
        try{

            Result<ChatPostMessageResponse, SlackError> postResult = slackClient.postMessage(
            ChatPostMessageParams.builder()
                .setText("Hello me! Here's a slack message!")
                .setChannelId(channelId)
                .build()
            ).join();

            return postResult.unwrapOrElseThrow();  

        }
        catch(IllegalStateException e){
            throw new SlackBotException(e);
        }

    }

}
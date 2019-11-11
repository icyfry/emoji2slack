package io.icyfry.bitbucket.e2s.impl;

import javax.inject.Inject;
import javax.inject.Named;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.hubspot.slack.client.SlackClientFactory;

import io.icyfry.bitbucket.e2s.api.Emoji2SlackException;
import io.icyfry.bitbucket.e2s.api.Emoji2SlackService;
import io.icyfry.bitbucket.e2s.api.bot.SlackBot;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotException;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotService;

@ExportAsService({ SlackBotService.class })
@Named
public class SlackBotServiceImpl implements SlackBotService {

    private static SlackBot bot;

    // Internal services

    @Inject
    public SlackBotServiceImpl() {
    }

    @Override
    public SlackBot getBot(Emoji2SlackService emoji2SlackService) throws SlackBotException, Emoji2SlackException {
        if (bot == null) {
            bot = createBot(emoji2SlackService);
        }
        return bot;
    }

    /**
     * Create a new Slack Bot instance
     * 
     * @return the bot instance
     * @throws SlackBotException
     * @throws Emoji2SlackException
     */
    private SlackBot createBot(Emoji2SlackService emoji2SlackService) throws SlackBotException, Emoji2SlackException {
        return new SlackBotImpl(
            SlackClientFactory.defaultFactory(),
            emoji2SlackService.getConfiguration().getBotAccessToken()
        );
    }
 
}
package io.icyfry.bitbucket.e2s.impl;

import javax.inject.Inject;
import javax.inject.Named;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.hubspot.slack.client.SlackClientFactory;

import io.icyfry.bitbucket.e2s.api.BotService;
import io.icyfry.bitbucket.e2s.api.bot.SlackBot;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotException;

@ExportAsService({ BotService.class })
@Named
public class SlackBotServiceImpl implements BotService {

    private static SlackBot bot;

    @Inject
    public SlackBotServiceImpl() {
    }

    @Override
    public SlackBot getBot() throws SlackBotException {
        if (bot == null) {
            bot = createBot();
        }
        return bot;
    }

    /**
     * Create a new Slack Bot instance
     * 
     * @return the bot instance
     * @throws SlackBotException
     */
    private SlackBot createBot() throws SlackBotException {
        return new SlackBotImpl(SlackClientFactory.defaultFactory());
    }
 
}
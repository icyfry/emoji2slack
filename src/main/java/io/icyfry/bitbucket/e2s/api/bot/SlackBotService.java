package io.icyfry.bitbucket.e2s.api.bot;

import io.icyfry.bitbucket.e2s.api.Emoji2SlackException;
import io.icyfry.bitbucket.e2s.api.Emoji2SlackService;
import io.icyfry.bitbucket.e2s.api.bot.SlackBot;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotException;

/**
 * Service class for slack bot related functions
 */
public interface SlackBotService
{

    /**
     * Retrive singleton instance of the bot
     * @return the bot instance
     * @throws SlackBotException
     * @throws Emoji2SlackException
     */
    public SlackBot getBot(Emoji2SlackService emoji2SlackService) throws SlackBotException, Emoji2SlackException;

}
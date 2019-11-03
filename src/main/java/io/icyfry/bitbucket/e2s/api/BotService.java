package io.icyfry.bitbucket.e2s.api;

import io.icyfry.bitbucket.e2s.api.bot.SlackBot;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotException;

/**
 * Service class for slack bot related functions
 */
public interface BotService
{

    /**
     * Retrive singleton instance of the bot
     * @return the bot instance
     * @throws SlackBotException
     */
    public SlackBot getBot() throws SlackBotException;

}
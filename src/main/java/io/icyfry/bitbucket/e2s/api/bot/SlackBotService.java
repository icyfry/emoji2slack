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
     * @param emoji2SlackService plugin main service
     * @throws SlackBotException error
     * @throws Emoji2SlackException error
     */
    public SlackBot getBot(Emoji2SlackService emoji2SlackService) throws SlackBotException, Emoji2SlackException;

    /**
     * Restart the bot (renew configuration)
     * @param emoji2SlackService plugin main service
     * @throws SlackBotException error
     * @throws Emoji2SlackException error
     */
    public void restartBot(Emoji2SlackService emoji2SlackService) throws SlackBotException, Emoji2SlackException;

}
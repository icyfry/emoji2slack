package io.icyfry.bitbucket.e2s.api;

import io.icyfry.bitbucket.e2s.impl.bot.Emoji2SlackBot;
import me.ramswaroop.jbot.core.slack.Bot;
public interface Emoji2SlackComponent
{
    public Emoji2SlackBot getBot();
}
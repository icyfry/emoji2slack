package io.icyfry.bitbucket.e2s.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import me.ramswaroop.jbot.core.common.JBot;
import me.ramswaroop.jbot.core.slack.Bot;


@JBot
@Profile("slack")
public class Emoji2SlackBot extends Bot {

    private static final Logger logger = LoggerFactory.getLogger(Emoji2SlackBot.class);

    @Override
    public String getSlackToken() {
        return null;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }

}
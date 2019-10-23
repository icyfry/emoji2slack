package io.icyfry.bitbucket.e2s.impl;

import javax.inject.Inject;
import javax.inject.Named;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;

import io.icyfry.bitbucket.e2s.api.Emoji2SlackComponent;
import io.icyfry.bitbucket.e2s.bot.Emoji2SlackBot;
import me.ramswaroop.jbot.core.slack.Bot;

@ExportAsService({ Emoji2SlackComponent.class })
@Named("Emoji2SlackComponent")
public class Emoji2SlackComponentImpl implements Emoji2SlackComponent {
    
    @ComponentImport
    private final ApplicationProperties applicationProperties;

    private Emoji2SlackBot bot;

    @Inject
    public Emoji2SlackComponentImpl(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public Bot getBot() {
        if(bot == null) {
            bot = new Emoji2SlackBot();
        }
        return bot;
    }
    
}
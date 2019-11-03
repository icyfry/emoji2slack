package io.icyfry.bitbucket.e2s.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import com.atlassian.bitbucket.event.pull.PullRequestCommentAddedEvent;
import com.atlassian.bitbucket.event.pull.PullRequestCommentEditedEvent;
import com.atlassian.bitbucket.event.pull.PullRequestCommentEvent;
import com.atlassian.event.api.EventListener;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;

import io.icyfry.bitbucket.e2s.api.BotService;
import io.icyfry.bitbucket.e2s.api.Configuration;
import io.icyfry.bitbucket.e2s.api.Emoji2SlackEventService;
import io.icyfry.bitbucket.e2s.api.Emoji2SlackService;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotException;

@ExportAsService({ Emoji2SlackEventService.class })
@Named
public class Emoji2SlackEventServiceImpl implements Emoji2SlackEventService{

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    // Internal services
    private final Emoji2SlackService emoji2SlackService;
    private final BotService botService;

    @Inject
    public Emoji2SlackEventServiceImpl(
        final Emoji2SlackService emoji2SlackService, 
        final ApplicationProperties applicationProperties,
        final BotService botService) {
        this.emoji2SlackService = checkNotNull(emoji2SlackService);
        this.applicationProperties = checkNotNull(applicationProperties);
        this.botService = checkNotNull(botService);
    }

    @EventListener
    public void onPullRequestCommentAddedEvent(PullRequestCommentAddedEvent event) {
        onPullRequestCommentEvent(event);
    }

    @EventListener
    public void onPullRequestCommentEditedEvent(PullRequestCommentEditedEvent event) {
        onPullRequestCommentEvent(event);
    }

    /**
     * Behaviour definition when commenting a pull request
     * @param event the commenting event
     */
    public void onPullRequestCommentEvent(PullRequestCommentEvent event) {

        // Valid events
        if (event instanceof PullRequestCommentAddedEvent || event instanceof PullRequestCommentEditedEvent) {

            // Read configuration of pull request repositories
            Collection<Configuration> configurations = emoji2SlackService.getConfigurationsInRepositories(
                    Arrays.asList(event.getPullRequest().getFromRef().getRepository(),
                            event.getPullRequest().getToRef().getRepository()));

            // Look if a configuration is matching the comment
            Configuration configuration = emoji2SlackService.findMatchingConfigurationForComment(event.getComment(),
                    configurations);

            // match found
            if (configuration != null) {
                try {
                    botService.getBot().sendMessage(configuration.getChannelId());
                } catch (SlackBotException e) {
                    // TODO Handle error
                }
            }

        }
        
    }

}
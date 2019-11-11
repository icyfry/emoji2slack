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

import io.icyfry.bitbucket.e2s.api.bot.SlackBotService;
import io.icyfry.bitbucket.e2s.api.Emoji2SlackEventService;
import io.icyfry.bitbucket.e2s.api.Emoji2SlackException;
import io.icyfry.bitbucket.e2s.api.Emoji2SlackService;
import io.icyfry.bitbucket.e2s.api.EmojiConfiguration;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotException;

@ExportAsService({ Emoji2SlackEventService.class })
@Named
public class Emoji2SlackEventServiceImpl implements Emoji2SlackEventService {

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    // Internal services
    private final Emoji2SlackService emoji2SlackService;
    private final SlackBotService botService;

    @Inject
    public Emoji2SlackEventServiceImpl(final Emoji2SlackService emoji2SlackService,
            final ApplicationProperties applicationProperties, final SlackBotService botService) {
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
     * 
     * @param event the commenting event
     */
    public void onPullRequestCommentEvent(PullRequestCommentEvent event) {

        // Valid events
        if (event instanceof PullRequestCommentAddedEvent || event instanceof PullRequestCommentEditedEvent) {

            // Read configuration of pull request repositories
            Collection<EmojiConfiguration> configurations = emoji2SlackService
                    .getEmojisConfigurationsInRepositories(Arrays.asList(event.getPullRequest().getFromRef().getRepository(),
                            event.getPullRequest().getToRef().getRepository()));

            // Look if a configuration is matching the comment
            try {

                EmojiConfiguration configuration = emoji2SlackService.findMatchingEmojiConfigurationForComment(event.getComment(),
                        configurations);

                // match found
                if (configuration != null) {
                    botService.getBot(this.emoji2SlackService).sendCommentMessage(configuration.getChannelId(),event.getComment());
                }

            } catch (SlackBotException e) {
                // TODO Handle error
            } catch (Emoji2SlackException e) {
                // TODO Handle error
            }

        }
        
    }

}
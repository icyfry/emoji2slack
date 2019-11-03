package io.icyfry.bitbucket.e2s.impl;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.bitbucket.comment.Comment;
import com.atlassian.bitbucket.emoticons.Emoticon;
import com.atlassian.bitbucket.emoticons.EmoticonService;
import com.atlassian.bitbucket.repository.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;

import io.icyfry.bitbucket.e2s.api.BotService;
import io.icyfry.bitbucket.e2s.api.Configuration;
import io.icyfry.bitbucket.e2s.api.Emoji2SlackService;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotException;
import io.icyfry.bitbucket.e2s.data.ConfigurationEntity;
import net.java.ao.DBParam;
import net.java.ao.Query;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService({ Emoji2SlackService.class })
@Named
public class Emoji2SlackServiceImpl implements Emoji2SlackService {

    @ComponentImport
    private final ActiveObjects ao;

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @ComponentImport
    private final EmoticonService emoticonService;

    // Internal services
    private final BotService botService;

    @Inject
    public Emoji2SlackServiceImpl(
        final ActiveObjects ao, 
        final ApplicationProperties applicationProperties,
        final BotService botService, 
        final EmoticonService emoticonService) {
        this.ao = checkNotNull(ao);
        this.applicationProperties = checkNotNull(applicationProperties);
        this.botService = checkNotNull(botService);
        this.emoticonService = checkNotNull(emoticonService);
    }

    @Override
    public Configuration findMatchingConfigurationForComment(Comment comment, Collection<Configuration> configurations) {
        Configuration matchingConfiguration= null;
        for (Configuration configuration : configurations) {
            if(configuration.getEmoji().getValue().isPresent()) {
                boolean containEmoji = comment.getText().contains(configuration.getEmoji().getValue().get());
                if(containEmoji) {
                    matchingConfiguration = configuration;
                    break;
                }
            }
        }
        return matchingConfiguration;
    }

    /**
     * Transform configurations entity object to model object
     * @param entity the ao entity to transform
     * @return the model object
     */
    private Configuration ConfigurationEntityToModel(ConfigurationEntity entity) {
        return new Configuration(
            entity.getChannelId(), 
            this.getAllEmojisAvailables().get(entity.getEmojiShortcut())
        );
    }

    /**
     * Transform a list of configurations entity object to a list of model object
     * @param entities the list of ao entities to transform
     * @return the list of model objects
     */
    private Collection<Configuration> ConfigurationEntityToModel(Collection<ConfigurationEntity> entities) {
        List<Configuration> configurations = newArrayList();
        for (ConfigurationEntity entity : entities) {
            configurations.add(ConfigurationEntityToModel(entity));
        }
        return configurations;
    }

    @Override
    public void saveConfiguration(String channelId, String emojiShortcut, int repositoryId) {
        final ConfigurationEntity entity = ao.create(ConfigurationEntity.class, new DBParam("CHANNEL_ID", channelId));
        entity.setEmojiShortcut(emojiShortcut);
        entity.setRepositoryId(repositoryId);
        entity.save();
        try{
            botService.getBot().sendMessage(channelId);
        } catch (SlackBotException e) {
            // TODO Handle error
        }
    }

    @Override
    public Collection<Configuration> getAllConfigurations() {
        List<ConfigurationEntity> entities = newArrayList(ao.find(ConfigurationEntity.class));
        return ConfigurationEntityToModel(entities);
    }

    @Override
    public Collection<Configuration> getConfigurationsByRepositoryId(int repositoryId) {
        List<ConfigurationEntity> entities = newArrayList(ao.find(ConfigurationEntity.class, Query.select().where("REPOSITORY_ID = "+repositoryId+"")));
        return ConfigurationEntityToModel(entities);
    }

    @Override
    public Map<String, Emoticon> getAllEmojisAvailables() {
        return emoticonService.getEmoticons();
    }

    @Override
    public Collection<Configuration> getConfigurationsInRepositories(List<Repository> repositories) {
       
        Collection<Configuration> configurations = newArrayList();
        for (Repository repository : repositories) {
            configurations.addAll(
            this.getConfigurationsByRepositoryId(repository.getId())
            );
        }


        // TODO Auto-generated method stub
        return null;
    }

}
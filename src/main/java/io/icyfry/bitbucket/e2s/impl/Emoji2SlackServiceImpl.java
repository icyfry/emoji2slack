package io.icyfry.bitbucket.e2s.impl;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.bitbucket.comment.Comment;
import com.atlassian.bitbucket.emoticons.Emoticon;
import com.atlassian.bitbucket.emoticons.EmoticonService;
import com.atlassian.bitbucket.repository.Repository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.slack.client.models.response.chat.ChatPostMessageResponse;

import io.icyfry.bitbucket.e2s.api.Emoji2SlackException;
import io.icyfry.bitbucket.e2s.api.Emoji2SlackService;
import io.icyfry.bitbucket.e2s.api.EmojiConfiguration;
import io.icyfry.bitbucket.e2s.api.GlobalConfiguration;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotException;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotService;
import io.icyfry.bitbucket.e2s.data.EmojiConfigEntity;
import net.java.ao.DBParam;
import net.java.ao.Query;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService({ Emoji2SlackService.class })
@Named
public class Emoji2SlackServiceImpl implements Emoji2SlackService {

    private static final String PLUGIN_STORAGE_KEY = "io.icyfry.bitbucket.emoji2slack";

    @ComponentImport
    private final ActiveObjects ao;

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @ComponentImport
    private final EmoticonService emoticonService;

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    // Internal services
    private final SlackBotService botService;

    @Inject
    public Emoji2SlackServiceImpl(final ActiveObjects ao, final ApplicationProperties applicationProperties,
            final SlackBotService botService, final EmoticonService emoticonService,
            PluginSettingsFactory pluginSettingsFactory) {
        this.ao = checkNotNull(ao);
        this.applicationProperties = checkNotNull(applicationProperties);
        this.botService = checkNotNull(botService);
        this.emoticonService = checkNotNull(emoticonService);
        this.pluginSettingsFactory = checkNotNull(pluginSettingsFactory);
    }

    @Override
    public EmojiConfiguration findMatchingEmojiConfigurationForComment(Comment comment,
            Collection<EmojiConfiguration> configurations) throws Emoji2SlackException {
        EmojiConfiguration matchingConfiguration = null;
        if (configurations == null) {
            throw new Emoji2SlackException("no configurations provided to find matches");
        }
        for (EmojiConfiguration configuration : configurations) {
            boolean containEmoji = comment.getText().contains(":" + configuration.getEmoji().getShortcut() + ":")
                    || (configuration.getEmoji().getValue().isPresent()
                            ? comment.getText().contains(configuration.getEmoji().getValue().get())
                            : false);
            if (containEmoji) {
                matchingConfiguration = configuration;
                break;
            }
        }
        return matchingConfiguration;
    }

    /**
     * Transform configurations entity object to model object
     * 
     * @param entity the ao entity to transform
     * @return the model object
     */
    private EmojiConfiguration ConfigurationEntityToModel(EmojiConfigEntity entity) {
        return new EmojiConfiguration(
            entity.getID(),
            entity.getChannelId(),
            this.getAllEmojisAvailables().getOrDefault(
                entity.getEmojiShortcut(), this.emoticonService.getEmoticons().get("question")
            ),
            entity.getRepositoryId()
        );
    }

    /**
     * Transform a list of configurations entity object to a list of model object
     * 
     * @param entities the list of ao entities to transform
     * @return the list of model objects
     */
    private Collection<EmojiConfiguration> ConfigurationEntityToModel(Collection<EmojiConfigEntity> entities) {
        List<EmojiConfiguration> configurations = newArrayList();
        for (EmojiConfigEntity entity : entities) {
            configurations.add(ConfigurationEntityToModel(entity));
        }
        return configurations;
    }

    @Override
    public void saveEmojiConfiguration(String channelId, String emojiShortcut, int repositoryId) throws Emoji2SlackException {

        // Preconditions
        checkNotNull(channelId);
        checkNotNull(emojiShortcut);
        checkNotNull(repositoryId);

        final EmojiConfigEntity entity = ao.create(EmojiConfigEntity.class, new DBParam("CHANNEL_ID", channelId));
        
        entity.setEmojiShortcut(emojiShortcut);
        entity.setRepositoryId(repositoryId);
        
        // Try contacting the channel
        ChatPostMessageResponse response = botService.getBot(this).sendConfigurationMessage(this.ConfigurationEntityToModel(entity));

        if(response.isOk()){
            entity.save();
        }
        else{
            throw new Emoji2SlackException("Error sending message to Slack");
        }

    }

    @Override
    public Collection<EmojiConfiguration> getAllEmojisConfigurations() {
        List<EmojiConfigEntity> entities = newArrayList(ao.find(EmojiConfigEntity.class));
        return ConfigurationEntityToModel(entities);
    }

    @Override
    public Collection<EmojiConfiguration> getEmojisConfigurationsByRepositoryId(int repositoryId) {
        List<EmojiConfigEntity> entities = newArrayList(
                ao.find(EmojiConfigEntity.class, Query.select().where("REPOSITORY_ID = " + repositoryId)));
        return ConfigurationEntityToModel(entities);
    }

    @Override
    public Map<String, Emoticon> getAllEmojisAvailables() {
        return emoticonService.getEmoticons();
    }

    @Override
    public Collection<EmojiConfiguration> getEmojisConfigurationsInRepositories(List<Repository> repositories) {
        Collection<EmojiConfiguration> configurations = newArrayList();
        for (Repository repository : repositories) {
            configurations.addAll(this.getEmojisConfigurationsByRepositoryId(repository.getId()));
        }
        return configurations;
    }

    @Override
    public void saveConfiguration(GlobalConfiguration input) throws Emoji2SlackException {
        ObjectMapper objectMapper = new ObjectMapper();

        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
        try {
            pluginSettings.put(PLUGIN_STORAGE_KEY + ".global", objectMapper.writeValueAsString(input));
        } catch (JsonProcessingException e) {
            throw new Emoji2SlackException(e);
        }
    }

    @Override
    public GlobalConfiguration getConfiguration() throws Emoji2SlackException {

        ObjectMapper objectMapper = new ObjectMapper();
        GlobalConfiguration configuration = null;
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();

        String configurationStored = (String) pluginSettings.get(PLUGIN_STORAGE_KEY + ".global");

        if (configurationStored == null) {
            // throw new Emoji2SlackException(PLUGIN_STORAGE_KEY + ".global setting is not
            // defined");
            configuration = new GlobalConfiguration();
        } else {
            try {
                configuration = objectMapper.readValue(configurationStored, GlobalConfiguration.class);
            } catch (IOException e) {
                throw new Emoji2SlackException(e);
            }
        }

        return configuration;

    }

    @Override
    public EmojiConfiguration getEmojiConfiguration(int id) throws Emoji2SlackException {
        List<EmojiConfigEntity> entities = newArrayList(ao.find(EmojiConfigEntity.class, Query.select().where("ID = " + id)));
        if(entities.size() == 1){
            return ConfigurationEntityToModel(entities.get(0));
        }
        else{
            throw new Emoji2SlackException("Error retriving an emoji configuration with id "+id + ", "+entities.size()+" results founds");
        }
    }

    @Override
    public void deleteEmojiConfiguration(int id) throws Emoji2SlackException {
        List<EmojiConfigEntity> entities = newArrayList(ao.find(EmojiConfigEntity.class, Query.select().where("ID = " + id)));
        if(entities.size() == 1){
            ao.delete(entities.get(0));
        }
        else{
            throw new Emoji2SlackException("Error retriving an emoji configuration with id "+id + ", "+entities.size()+" results founds");
        }
    }

}
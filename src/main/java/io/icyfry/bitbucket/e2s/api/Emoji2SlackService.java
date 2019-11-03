package io.icyfry.bitbucket.e2s.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.atlassian.bitbucket.comment.Comment;
import com.atlassian.bitbucket.emoticons.Emoticon;
import com.atlassian.bitbucket.repository.Repository;

import io.icyfry.bitbucket.e2s.api.bot.SlackBotException;

/**
 * Main Service class for plugin. manage the bot behaviour configuration
 */
public interface Emoji2SlackService
{

    /**
     * Return all emojis availables to be linked to a slack channel
     * @return the list of emojis
     */
    public Map<String, Emoticon> getAllEmojisAvailables();

    /**
     * Return all stored configurations for channel linked to an emoji
     * @return the list of all configurations
     */
    public Collection<Configuration> getAllConfigurations();

    /**
     * Return all stored configurations for channel linked to an emoji for a list of repositories
     * @param repositories the repositories linked to configurations
     * @return the list of configurations
     */
    public Collection<Configuration> getConfigurationsInRepositories(List<Repository> repositories);

    /**
     * Return all stored configurations for channel linked to an emoji for a specific repository 
     * @param repositoryId the id of the repository 
     * @return the list of configurations
     */
	public Collection<Configuration> getConfigurationsByRepositoryId(int repositoryId);

    /**
     * Save a new configuration
     * @param channelId the id of the channel to link
     * @param emoji the emoji to link to the channel
     * @param repositoryId the repository related to the configuration
     */
    public void saveConfiguration(String channelId, String emojiShortcut, int repositoryId);
   
    /**
     * Look if the comment contains an emoji linked to a channel in the plugin
     * configuration.
     * 
     * @param comment the comment to look at
     * @return the matching configuration, else return null
     */
    public Configuration findMatchingConfigurationForComment(Comment comment, Collection<Configuration> configurations);


}
package io.icyfry.bitbucket.e2s.api;

import com.atlassian.bitbucket.emoticons.Emoticon;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A slack channel configuration
 */
public class EmojiConfiguration {

    // id of the configuration (if stored)
    @JsonProperty(required=false)
    private int id;

    // emoji linked to the channel
    @JsonSerialize(using = EmoticonSerializer.class)
    private Emoticon emoji;

    // id of the channel
    @JsonProperty
    private String channelId;

    public EmojiConfiguration(int id, String channelId, Emoticon emoji) {
        this.id = id;
        this.emoji = emoji;
        this.channelId = channelId;
    }

	public Emoticon getEmoji() {
        return emoji;
    }

    public void setEmoji(Emoticon emoji) {
        this.emoji = emoji;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
package io.icyfry.bitbucket.e2s.web.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestInputAddEmojiConfiguration {

    @JsonProperty
    private String channelId;

    @JsonProperty
    private String emojiShortcut;

    @JsonProperty
    private int repositoryId;

	public RestInputAddEmojiConfiguration() {
	}

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getEmojiShortcut() {
        return emojiShortcut;
    }

    public void setEmojiShortcut(String emojiShortcut) {
        this.emojiShortcut = emojiShortcut;
    }

    public int getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(int repositoryId) {
        this.repositoryId = repositoryId;
    }

}
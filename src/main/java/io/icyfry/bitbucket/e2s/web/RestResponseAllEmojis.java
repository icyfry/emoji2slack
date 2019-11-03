package io.icyfry.bitbucket.e2s.web;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.bitbucket.emoticons.Emoticon;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.icyfry.bitbucket.e2s.api.EmoticonSerializer;

@JsonRootName(value = "emojis")
public class RestResponseAllEmojis {

    @JsonSerialize(contentUsing = EmoticonSerializer.class)
    private Map<String, Emoticon> emojis = new HashMap<>();

	public RestResponseAllEmojis(Map<String, Emoticon> emojis) {
        this.emojis = emojis;
	}

    public Map<String, Emoticon> getEmojis() {
        return emojis;
    }

    public void setEmojis(Map<String, Emoticon> emojis) {
        this.emojis = emojis;
    }

}
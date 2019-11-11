package io.icyfry.bitbucket.e2s.web.output;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.atlassian.bitbucket.emoticons.Emoticon;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.icyfry.bitbucket.e2s.api.EmoticonSerializer;

@JsonRootName(value = "emojis")
public class RestResponseAllEmojis {

    @JsonSerialize(contentUsing = EmoticonSerializer.class)
    private SortedMap<String, Emoticon> emojis = new TreeMap<>();

	public RestResponseAllEmojis(Map<String, Emoticon> emojis) {
        this.emojis = new TreeMap<>(emojis);
	}

    public SortedMap<String, Emoticon> getEmojis() {
        return emojis;
    }

    public void setEmojis(SortedMap<String, Emoticon> emojis) {
        this.emojis = emojis;
    }

}
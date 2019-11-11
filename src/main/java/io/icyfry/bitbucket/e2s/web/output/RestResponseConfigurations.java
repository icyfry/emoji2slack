package io.icyfry.bitbucket.e2s.web.output;

import java.util.ArrayList;
import java.util.Collection;

import io.icyfry.bitbucket.e2s.api.EmojiConfiguration;

public class RestResponseConfigurations {

    private Collection<EmojiConfiguration> configurations = new ArrayList<>();

	public RestResponseConfigurations(Collection<EmojiConfiguration> configurations) {
        this.configurations = configurations;
	}

    public Collection<EmojiConfiguration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Collection<EmojiConfiguration> configurations) {
        this.configurations = configurations;
    }

}
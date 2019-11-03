package io.icyfry.bitbucket.e2s.web;

import java.util.ArrayList;
import java.util.Collection;

import io.icyfry.bitbucket.e2s.api.Configuration;

public class RestResponseConfigurations {

    private Collection<Configuration> configurations = new ArrayList<>();

	public RestResponseConfigurations(Collection<Configuration> configurations) {
        this.configurations = configurations;
	}

    public Collection<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Collection<Configuration> configurations) {
        this.configurations = configurations;
    }

}
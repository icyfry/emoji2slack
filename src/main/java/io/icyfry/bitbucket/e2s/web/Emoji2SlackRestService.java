package io.icyfry.bitbucket.e2s.web;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.icyfry.bitbucket.e2s.api.Emoji2SlackException;
import io.icyfry.bitbucket.e2s.api.Emoji2SlackService;
import io.icyfry.bitbucket.e2s.api.EmojiConfiguration;
import io.icyfry.bitbucket.e2s.web.input.RestInputAddEmojiConfiguration;
import io.icyfry.bitbucket.e2s.web.input.RestInputSetGlobalConfiguration;
import io.icyfry.bitbucket.e2s.web.output.RestResponseAllEmojis;
import io.icyfry.bitbucket.e2s.web.output.RestResponseConfigurations;

@Path("/")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class Emoji2SlackRestService {

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    // Internal services
    private final Emoji2SlackService emoji2SlackService;

    @Inject
    public Emoji2SlackRestService(final Emoji2SlackService emoji2SlackService,
            final ApplicationProperties applicationProperties) {
        this.emoji2SlackService = checkNotNull(emoji2SlackService);
        this.applicationProperties = checkNotNull(applicationProperties);
    }

    /**
     * Return all the emojis that can be used in bitbucket
     * 
     * @return list of all emojis
     */
    @GET
    @Path("emojis")
    public Response getAllEmojis() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Response
                    .ok(objectMapper
                            .writeValueAsString(new RestResponseAllEmojis(emoji2SlackService.getAllEmojisAvailables())))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @GET
    @Path("emojis/configurations/{repositoryid}")
    public Response getConfigurations(@PathParam("repositoryid") String repositoryid) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Collection<EmojiConfiguration> configurations;
            if (repositoryid == null) {
                configurations = emoji2SlackService.getAllEmojisConfigurations();
            } else {
                configurations = emoji2SlackService
                        .getEmojisConfigurationsByRepositoryId(Integer.valueOf(repositoryid));
            }
            return Response.ok(objectMapper.writeValueAsString(new RestResponseConfigurations(configurations))).build();

        } catch (JsonProcessingException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @POST
    @Path("emojis/configurations/add")
    public Response addConfiguration(RestInputAddEmojiConfiguration input) {
        try {

            emoji2SlackService.saveEmojiConfiguration(
                input.getChannelId(), 
                input.getEmojiShortcut(),
                input.getRepositoryId()
            );

            return Response.ok("configuration saved").build();

        } catch (Emoji2SlackException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }

    }

    @POST
    @Path("configuration")
    public Response setConfiguration(RestInputSetGlobalConfiguration input) {
        try{
            
            emoji2SlackService.saveConfiguration(input);
            return Response.ok("plugin global configuration saved").build();

        } catch (Emoji2SlackException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @GET
    @Path("configuration")
    public Response getConfiguration() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Response.ok(objectMapper.writeValueAsString(emoji2SlackService.getConfiguration())).build();
        } catch (JsonProcessingException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        } catch (Emoji2SlackException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

}
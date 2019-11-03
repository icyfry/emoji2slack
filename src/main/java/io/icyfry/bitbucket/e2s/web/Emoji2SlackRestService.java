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

import io.icyfry.bitbucket.e2s.api.BotService;
import io.icyfry.bitbucket.e2s.api.Configuration;
import io.icyfry.bitbucket.e2s.api.Emoji2SlackService;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotException;

@Path("/")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class Emoji2SlackRestService {

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    // Internal services
    private final Emoji2SlackService emoji2SlackService;
    private final BotService botService;

    @Inject
    public Emoji2SlackRestService(final Emoji2SlackService emoji2SlackService,
            final ApplicationProperties applicationProperties, final BotService botService) {
        this.emoji2SlackService = checkNotNull(emoji2SlackService);
        this.applicationProperties = checkNotNull(applicationProperties);
        this.botService = checkNotNull(botService);
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

// @PathParam

    @GET
    @Path("configurations/list")
    public Response getConfigurations(@QueryParam("repositoryid") String repositoryid) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Collection<Configuration> configurations;
            if(repositoryid == null){
                configurations = emoji2SlackService.getAllConfigurations();
            }
            else {
                configurations = emoji2SlackService.getConfigurationsByRepositoryId(Integer.valueOf(repositoryid));
            }
            return Response.ok(objectMapper
                    .writeValueAsString(new RestResponseConfigurations(configurations)))
                    .build();

        } catch (JsonProcessingException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @POST
    @Path("configurations/add")
    public Response addConfiguration(RestInputAddConfiguration input) {

        emoji2SlackService.saveConfiguration(
            input.getChannelId(), 
            input.getEmojiShortcut(),
            input.getRepositoryId());

        return Response.ok("configuration saved").build();

    }

}
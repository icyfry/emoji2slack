package io.icyfry.bitbucket.e2s.web;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.atlassian.bitbucket.auth.AuthenticationContext;
import com.atlassian.bitbucket.permission.Permission;
import com.atlassian.bitbucket.permission.PermissionService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.icyfry.bitbucket.e2s.api.Emoji2SlackException;
import io.icyfry.bitbucket.e2s.api.Emoji2SlackSecurityException;
import io.icyfry.bitbucket.e2s.api.Emoji2SlackService;
import io.icyfry.bitbucket.e2s.api.EmojiConfiguration;
import io.icyfry.bitbucket.e2s.api.GlobalConfiguration;
import io.icyfry.bitbucket.e2s.web.input.RestInputAddEmojiConfiguration;
import io.icyfry.bitbucket.e2s.web.input.RestInputSetGlobalConfiguration;
import io.icyfry.bitbucket.e2s.web.output.RestResponseAllEmojis;
import io.icyfry.bitbucket.e2s.web.output.RestResponseConfigurations;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;


@Path("/")
@OpenAPIDefinition(
    info = @Info(
        description = "Emoji2Slack Bitbucket plugin operations",
        title = "Emoji2Slack API",
        version = "1.0"
    )
)
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class Emoji2SlackRestService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @ComponentImport
    private final AuthenticationContext authenticationContext;

    @ComponentImport
    private final PermissionService permissionService;

    // Internal services
    private final Emoji2SlackService emoji2SlackService;

    @Inject
    public Emoji2SlackRestService(
        final Emoji2SlackService emoji2SlackService,
        final ApplicationProperties applicationProperties, 
        final AuthenticationContext authenticationContext, 
        final PermissionService permissionService) {
        this.emoji2SlackService = checkNotNull(emoji2SlackService);
        this.applicationProperties = checkNotNull(applicationProperties);
        this.authenticationContext = checkNotNull(authenticationContext);
        this.permissionService = checkNotNull(permissionService);
    }

    /**
     * Response in case of error
     * @param e exception catched
     * @return the Error response
     */
    private Response createErrorResponse(Exception e) {
        try {
            if(e instanceof Emoji2SlackSecurityException){
                // No details in case of security problem
                return Response.status(Status.FORBIDDEN).build();
            }
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(objectMapper.writeValueAsString(e)).build();
        } catch (JsonProcessingException jsone) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * @see checkSecurity(Permission permission,Integer repositoryId)
     */
    private void checkSecurity(Permission permission) throws Emoji2SlackSecurityException {
        checkSecurity(permission,null);
    }

    /**
     * Check security for API access
     * @param permission require a permission
     * @param repositoryId the repository where apply the permission
     * @throws Emoji2SlackSecurityException
     */
    private void checkSecurity(Permission permission,Integer repositoryId) throws Emoji2SlackSecurityException {
        if(!authenticationContext.isAuthenticated()) {
            throw new Emoji2SlackSecurityException("You are not connected");
        }
        if(repositoryId == null && !permissionService.hasGlobalPermission(permission)){
            throw new Emoji2SlackSecurityException("You need global permission");
        }
        if(repositoryId != null && !permissionService.hasRepositoryPermission(repositoryId, permission)){
            throw new Emoji2SlackSecurityException("You need repository permission");
        }
    }

    /**
     * Return all the emojis that can be used in bitbucket
     * 
     * @return list of all emojis
     */
    @GET
    @Path("emojis")
    @Operation(summary = "Return the list of all emojis",
        responses = {
            @ApiResponse(
                description = "The list of all emojis",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestResponseAllEmojis.class))
                )
        }
    )
    public Response getAllEmojis() {
        try {
            checkSecurity(Permission.LICENSED_USER);
            return Response
                    .ok(objectMapper
                            .writeValueAsString(new RestResponseAllEmojis(emoji2SlackService.getAllEmojisAvailables())))
                    .build();
        } catch (JsonProcessingException|RuntimeException|Emoji2SlackSecurityException e) {
            return createErrorResponse(e);
        }
    }

    @GET
    @Path("emojis/configurations/{repositoryid}")
    @Operation(summary = "Return the emojis configurations related to the plugin on a specific repository",
        responses = {
            @ApiResponse(
                description = "The configurations",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestResponseConfigurations.class))
                )
        }
    )
    public Response getConfigurations(@Parameter(description = "The id of the repository", required = false) @PathParam("repositoryid") String repositoryid) {
        try {

            checkSecurity(Permission.REPO_ADMIN,Integer.valueOf(repositoryid));

            Collection<EmojiConfiguration> configurations;

            if (repositoryid == null) {
                configurations = emoji2SlackService.getAllEmojisConfigurations();
            } else {
                configurations = emoji2SlackService
                        .getEmojisConfigurationsByRepositoryId(Integer.valueOf(repositoryid));
            }

            return Response.ok(objectMapper.writeValueAsString(new RestResponseConfigurations(configurations))).build();

        } catch (JsonProcessingException|RuntimeException|Emoji2SlackSecurityException e) {
            return createErrorResponse(e);
        }
    }

    @POST
    @Path("emojis/configurations/add")
    @Operation(summary = "Add a new emoji configuration")
    public Response addConfiguration(@Parameter(description = "The configuration to add", required = true) RestInputAddEmojiConfiguration input) {
        try {

            checkSecurity(Permission.REPO_ADMIN,Integer.valueOf(input.getRepositoryId()));

            emoji2SlackService.saveEmojiConfiguration(
                input.getChannelId(), 
                input.getEmojiShortcut(),
                input.getRepositoryId()
            );
            
            return Response.ok("Configuration saved").build();

        } catch (Emoji2SlackException|RuntimeException e) {
            return createErrorResponse(e);
        }

    }

    @DELETE
    @Path("emojis/configuration/{id}")
    @Operation(summary = "Delete a emoji configuration (with internal id)")
    public Response addConfiguration(@Parameter(description = "The id of the configuration to delete", required = true) @PathParam("id") String id) {
        try {

            // Security check
            EmojiConfiguration emojiConfiguration = emoji2SlackService.getEmojiConfiguration(Integer.valueOf(id));
            checkSecurity(Permission.REPO_ADMIN,emojiConfiguration.getRepositoryId());
            
            emoji2SlackService.deleteEmojiConfiguration(Integer.valueOf(id));
            return Response.ok("Configuration deleted").build();

        } catch (Emoji2SlackException|RuntimeException e) {
            return createErrorResponse(e);
        }

    }

    @POST
    @Path("configuration")
    @Operation(summary = "Modify the global configuration of the plugin")
    public Response setConfiguration(@Parameter(description = "The new configuration", required = true) RestInputSetGlobalConfiguration input) {
        try{

            checkSecurity(Permission.ADMIN);

            emoji2SlackService.saveConfiguration(input);

            return Response.ok("plugin global configuration saved").build();

        } catch (Emoji2SlackException|RuntimeException e) {
            return createErrorResponse(e);
        }
    }

    @GET
    @Path("configuration")
    @Operation(summary = "Return the global configuration of the plugin",
        responses = {
            @ApiResponse(
                description = "The global configuration of the plugin",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GlobalConfiguration.class))
                )
        }
    )
    public Response getConfiguration() {
        try {

            checkSecurity(Permission.ADMIN);
            GlobalConfiguration config = emoji2SlackService.getConfiguration();

            // Default configuration value shown
            if(config.getBotAccessToken() == null) config.setBotAccessToken("");

            return Response.ok(objectMapper.writeValueAsString(config)).build();

        } catch (JsonProcessingException|Emoji2SlackException|RuntimeException e) {
            return createErrorResponse(e);
        }
    }

}
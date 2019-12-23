package io.icyfry.bitbucket.e2s.web;

import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.bitbucket.auth.AuthenticationContext;
import com.atlassian.bitbucket.permission.Permission;
import com.atlassian.bitbucket.permission.PermissionService;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.UrlMode;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Emoji2SlackRepositorySettingsServlet extends AbstractServlet {

    private static final long serialVersionUID = 1L;

    private final RepositoryService repositoryService;

    private final ApplicationProperties applicationProperties;

    private final AuthenticationContext authenticationContext;

    private final PermissionService permissionService;

    @Inject
    public Emoji2SlackRepositorySettingsServlet(
        @ComponentImport SoyTemplateRenderer soyTemplateRenderer,
        @ComponentImport RepositoryService repositoryService,
        @ComponentImport ApplicationProperties applicationProperties,
        @ComponentImport AuthenticationContext authenticationContext,
        @ComponentImport PermissionService permissionService
        ) {
        super(soyTemplateRenderer);
        this.repositoryService = checkNotNull(repositoryService);
        this.applicationProperties = checkNotNull(applicationProperties);
        this.authenticationContext = checkNotNull(authenticationContext);
        this.permissionService = checkNotNull(permissionService);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        // Security check
        if(!authenticationContext.isAuthenticated()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Get repoSlug from path
        String pathInfo = req.getPathInfo();

        String[] components = pathInfo.split("/");

        if (components.length < 3) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Repository repository = repositoryService.getBySlug(components[1], components[2]);

        if (repository == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (components.length == 4 && "settings".equalsIgnoreCase(components[3])) {
            
            // Security check
            if(!permissionService.hasRepositoryPermission(repository, Permission.REPO_ADMIN)){
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            // Data sent to soy template
            Map<String, Object> data = new HashMap<>();
            data.put("repository", repository);
            data.put("contextPath", applicationProperties.getBaseUrl(UrlMode.AUTO));

            render(resp, "emoji2slack.repositorySettings", data);

        }
        else {

            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;

        }
        
    }

}

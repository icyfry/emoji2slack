package io.icyfry.bitbucket.e2s.web;

import com.atlassian.soy.renderer.SoyTemplateRenderer;
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

public class Emoji2SlackGlobalSettingsServlet extends AbstractServlet {

    private static final long serialVersionUID = 1L;

    private final ApplicationProperties applicationProperties;

    @Inject
    public Emoji2SlackGlobalSettingsServlet(
        @ComponentImport SoyTemplateRenderer soyTemplateRenderer,
        @ComponentImport ApplicationProperties applicationProperties
        ) {
        super(soyTemplateRenderer);
        this.applicationProperties = checkNotNull(applicationProperties);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get repoSlug from path
        String pathInfo = req.getPathInfo();

        String[] components = pathInfo.split("/");

        if (components.length == 2 && "settings".equalsIgnoreCase(components[1])) {

            // Data sent to soy template
            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", applicationProperties.getBaseUrl(UrlMode.AUTO));

            render(resp, "emoji2slack.globalSettings", data);
        
        }
        else{

            // TODO About plugin
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;

        }

    }

}

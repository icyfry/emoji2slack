package it.io.icyfry.bitbucket.e2s;

import static org.junit.Assert.assertNotNull;

import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.atlassian.sal.api.ApplicationProperties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.socket.WebSocketSession;

import io.icyfry.bitbucket.e2s.api.Emoji2SlackComponent;
import io.icyfry.bitbucket.e2s.bot.Emoji2SlackBot;
import me.ramswaroop.jbot.core.slack.SlackService;

//@RunWith(AtlassianPluginsTestRunner.class)
public class Emoji2SlackComponentWiredTest
{
    private final ApplicationProperties applicationProperties;
    private final Emoji2SlackComponent myPluginComponent;

    @Mock
    private WebSocketSession session;

    @Mock
    private SlackService slackService;

    @InjectMocks
    private Emoji2SlackBot bot;

    public Emoji2SlackComponentWiredTest(ApplicationProperties applicationProperties,Emoji2SlackComponent myPluginComponent)
    {
        this.applicationProperties = applicationProperties;
        this.myPluginComponent = myPluginComponent;
    }
/*
    @Test
    public void testSlackService()
    {
        assertNotNull(slackService);
    }
*/
}
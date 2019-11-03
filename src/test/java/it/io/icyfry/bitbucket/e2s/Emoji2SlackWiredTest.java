package it.io.icyfry.bitbucket.e2s;

import static org.junit.Assert.assertNotNull;

import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
//import com.atlassian.sal.api.ApplicationProperties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
//import org.springframework.web.socket.WebSocketSession;

import io.icyfry.bitbucket.e2s.api.bot.SlackBot;

//import me.ramswaroop.jbot.core.slack.SlackService;

//@RunWith(AtlassianPluginsTestRunner.class)
public class Emoji2SlackWiredTest
{
    //private final ApplicationProperties applicationProperties;
/*
    @Mock
    private WebSocketSession session;

    @Mock
    private SlackService slackService;
*/
    @InjectMocks
    private SlackBot bot;

    public Emoji2SlackWiredTest(/*ApplicationProperties applicationProperties*/)
    {
        //this.applicationProperties = applicationProperties;
    }
/*
    @Test
    public void testSlackService()
    {
        assertNotNull(slackService);
    }
*/
}
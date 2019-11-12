package ut.io.icyfry.bitbucket.e2s;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.atlassian.sal.api.ApplicationProperties;
import com.hubspot.slack.client.SlackClient;
import com.hubspot.slack.client.SlackClientFactory;
import com.hubspot.slack.client.SlackClientRuntimeConfig;
import com.hubspot.slack.client.SlackWebClient;
import com.hubspot.slack.client.models.response.chat.ChatPostMessageResponse;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.mockito.invocation.InvocationOnMock;

import io.icyfry.bitbucket.e2s.api.bot.SlackBotException;
import io.icyfry.bitbucket.e2s.impl.SlackBotImpl;

/**
 * UTs related to the Emoji2Slack bot
 */
public class Emoji2SlackBotUnitTest {

    private static final String SLACK_TOKEN = "xoxp-test-token";

    @Mock
    private SlackWebClient.Factory slackWebClientFactory;

    @Mock
    private ApplicationProperties applicationProperties;

    // Bot used for tests
    private SlackBotImpl bot;
    
    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);

        doAnswer(new Answer<SlackClient>() {
            public SlackClient answer(InvocationOnMock invocation) {
               
                SlackWebClient slackClient = (SlackWebClient) SlackClientFactory.defaultFactory().create(SlackClientRuntimeConfig.builder()
                .setTokenSupplier(() -> SLACK_TOKEN)
                .setRequestDebugger((requestId, method, request) -> {})
                .build());

                return slackClient;

            }
        }).when(slackWebClientFactory).build(any());

        this.bot = new SlackBotImpl(slackWebClientFactory, SLACK_TOKEN);

    }

    @Test
    @Ignore
    public void testBotSendMessage() throws SlackBotException {
        ChatPostMessageResponse result = this.bot.sendMessage("test","hello!");
        assertTrue(result.isOk());
    }

}
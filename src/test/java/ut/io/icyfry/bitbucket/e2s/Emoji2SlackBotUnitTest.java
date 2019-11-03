package ut.io.icyfry.bitbucket.e2s;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.ApplicationProperties;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.icyfry.bitbucket.e2s.api.BotService;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotException;

/**
 * UTs related to the Emoji2Slack plugin (bot)
 */
public class Emoji2SlackBotUnitTest {

    @Mock private ActiveObjects ao;

    @Mock private ApplicationProperties applicationProperties;

    @Mock private BotService botService;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Ignore
    public void testBotSendMessage() throws SlackBotException {
        // TODO
    }

}
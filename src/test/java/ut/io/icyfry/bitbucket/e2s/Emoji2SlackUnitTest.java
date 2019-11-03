package ut.io.icyfry.bitbucket.e2s;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.bitbucket.emoticons.Emoticon;
import com.atlassian.bitbucket.emoticons.EmoticonService;
import com.atlassian.sal.api.ApplicationProperties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.icyfry.bitbucket.e2s.api.BotService;
import io.icyfry.bitbucket.e2s.api.Configuration;
import io.icyfry.bitbucket.e2s.impl.Emoji2SlackServiceImpl;

/**
 * UTs related to the Emoji2Slack plugin
 */
public class Emoji2SlackUnitTest {

    @Mock
    private ActiveObjects ao;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private BotService botService;

    @Mock
    private EmoticonService emoticonService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindMatchingConfigurationForComment() {

        Emoji2SlackServiceImpl service = new Emoji2SlackServiceImpl(ao,applicationProperties,botService,emoticonService);

        // Mock comment
        MockComment testComment = new MockComment("👏 Hello");

        // Mock configuration
        Emoticon emoticon = new MockEmoticon("clap","👏");

        Collection<Configuration> configurations = new ArrayList<Configuration>();
        configurations.add(
            new Configuration("001", emoticon)
        );

        // Look if configuration is matching
        Configuration configurationFound = service.findMatchingConfigurationForComment(testComment,configurations);
        
        assertNotNull(configurationFound);
        assertEquals("👏",configurationFound.getEmoji().getValue().get());
        assertEquals("001",configurationFound.getChannelId());

    }
    
}
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

import io.icyfry.bitbucket.e2s.api.EmojiConfiguration;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotService;
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
    private SlackBotService botService;

    @Mock
    private EmoticonService emoticonService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindMatchingConfigurationForComment() throws Exception{

        Emoji2SlackServiceImpl service = new Emoji2SlackServiceImpl(ao,applicationProperties,botService,emoticonService);

        // Mock comment
        MockComment testComment = new MockComment(":clap: Hello");
        MockComment testComment2 = new MockComment("👏 Hello");

        // Mock configuration
        Emoticon emoticon = new MockEmoticon("clap","👏");

        Collection<EmojiConfiguration> configurations = new ArrayList<EmojiConfiguration>();
        configurations.add(
            new EmojiConfiguration("x", emoticon)
        );

        // Look if configuration is matching
        
        // Comment 1
        EmojiConfiguration configurationFound = service.findMatchingEmojiConfigurationForComment(testComment,configurations);

        // Comment 2
        EmojiConfiguration configurationFound2 = service.findMatchingEmojiConfigurationForComment(testComment2,configurations);
        
        assertNotNull(configurationFound);
        assertEquals("👏",configurationFound.getEmoji().getValue().get());
        assertEquals("clap",configurationFound.getEmoji().getShortcut());
        assertEquals("x",configurationFound.getChannelId());

        assertNotNull(configurationFound2);
        assertEquals("👏",configurationFound2.getEmoji().getValue().get());
        assertEquals("clap",configurationFound2.getEmoji().getShortcut());
        assertEquals("x",configurationFound2.getChannelId());

    }
    
}
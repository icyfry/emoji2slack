package ut.io.icyfry.bitbucket.e2s;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.bitbucket.emoticons.Emoticon;
import com.atlassian.bitbucket.emoticons.EmoticonService;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.icyfry.bitbucket.e2s.api.Emoji2SlackException;
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

    @Mock
    private PluginSettingsFactory pluginSettingsFactory;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = Emoji2SlackException.class) 
    public void testFindMatchingConfigurationForCommentError() throws Exception{

        Emoji2SlackServiceImpl service = new Emoji2SlackServiceImpl(ao,applicationProperties,botService,emoticonService,pluginSettingsFactory);

        // Should throw error
        service.findMatchingEmojiConfigurationForComment(new MockComment("..."),null);

    }

    @Test
    public void testFindMatchingConfigurationForComment() throws Exception{

        Emoji2SlackServiceImpl service = new Emoji2SlackServiceImpl(ao,applicationProperties,botService,emoticonService,pluginSettingsFactory);

        // Mock comment
        MockComment testComment = new MockComment(":clap: Hello"); // comment with shortcut (bitbucket)
        MockComment testComment2 = new MockComment("👏 Hello");   // comment with char (generic)
        MockComment testComment3 = new MockComment("no emoji");   // comment with no emoji

        // Mock configuration
        Emoticon emoticon = new MockEmoticon("clap","👏");

        Collection<EmojiConfiguration> configurations = new ArrayList<EmojiConfiguration>();
        configurations.add(
            new EmojiConfiguration(0, "x", emoticon)
        );

        // Look if configuration is matching
        
        // Comment 1
        EmojiConfiguration configurationFound = service.findMatchingEmojiConfigurationForComment(testComment,configurations);

        // Comment 2
        EmojiConfiguration configurationFound2 = service.findMatchingEmojiConfigurationForComment(testComment2,configurations);

        // Comment 3
        EmojiConfiguration configurationFound3 = service.findMatchingEmojiConfigurationForComment(testComment3,configurations);
        
        assertNotNull(configurationFound);
        assertEquals("👏",configurationFound.getEmoji().getValue().get());
        assertEquals("clap",configurationFound.getEmoji().getShortcut());
        assertEquals("x",configurationFound.getChannelId());

        assertNotNull(configurationFound2);
        assertEquals("👏",configurationFound2.getEmoji().getValue().get());
        assertEquals("clap",configurationFound2.getEmoji().getShortcut());
        assertEquals("x",configurationFound2.getChannelId());

        assertNull(configurationFound3);

    }
    
}
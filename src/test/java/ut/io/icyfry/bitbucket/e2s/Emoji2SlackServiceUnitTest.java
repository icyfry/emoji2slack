package ut.io.icyfry.bitbucket.e2s;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.bitbucket.emoticons.Emoticon;
import com.atlassian.bitbucket.emoticons.EmoticonService;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import io.icyfry.bitbucket.e2s.api.Emoji2SlackException;
import io.icyfry.bitbucket.e2s.api.EmojiConfiguration;
import io.icyfry.bitbucket.e2s.api.bot.SlackBotService;
import io.icyfry.bitbucket.e2s.data.EmojiConfigEntity;
import io.icyfry.bitbucket.e2s.impl.Emoji2SlackServiceImpl;

/**
 * UTs related to the Emoji2Slack main service
 */
public class Emoji2SlackServiceUnitTest {

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

    // Service used for tests
    private Emoji2SlackServiceImpl emoji2SlackService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        this.emoji2SlackService = new Emoji2SlackServiceImpl(ao,applicationProperties,botService,emoticonService,pluginSettingsFactory);
    }

    @Test
    public void testDeleteEmojiConfiguration() throws Exception{

        // Mock ao.find(...)
        doAnswer(new Answer<EmojiConfigEntity[]>() {
            public EmojiConfigEntity[] answer(InvocationOnMock invocation) {
                EmojiConfigEntity[] tab = new EmojiConfigEntity[1];
                tab[0] = null;
                return tab;
            }
        }).when(ao).find(any(),any());

        // Mock ao.delete(...)
        doNothing().when(ao).delete(isA(EmojiConfigEntity.class));

        emoji2SlackService.deleteEmojiConfiguration(1);

        // Nothing to assert

    }

    @Test(expected = Emoji2SlackException.class) 
    public void testFindMatchingConfigurationForCommentError() throws Exception{
        // Should throw error
        emoji2SlackService.findMatchingEmojiConfigurationForComment(new MockComment("..."),null);
    }

    @Test
    public void testFindMatchingConfigurationForComment() throws Exception{

        // Mock comment
        MockComment testComment = new MockComment(":clap: Hello"); // comment with shortcut (bitbucket)
        MockComment testComment2 = new MockComment("👏 Hello");   // comment with char (generic)
        MockComment testComment3 = new MockComment("no emoji");   // comment with no emoji

        // Mock configuration
        Emoticon emoticon = new MockEmoticon("clap","👏");

        Collection<EmojiConfiguration> configurations = new ArrayList<EmojiConfiguration>();
        configurations.add(
            new EmojiConfiguration(0, "x", emoticon, 0)
        );

        // Look if configuration is matching
        
        // Comment 1
        EmojiConfiguration configurationFound = emoji2SlackService.findMatchingEmojiConfigurationForComment(testComment,configurations);

        // Comment 2
        EmojiConfiguration configurationFound2 = emoji2SlackService.findMatchingEmojiConfigurationForComment(testComment2,configurations);

        // Comment 3
        EmojiConfiguration configurationFound3 = emoji2SlackService.findMatchingEmojiConfigurationForComment(testComment3,configurations);
        
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

    @Test(expected = NullPointerException.class) 
    public void testSaveEmojiConfigurationError() throws Exception{
        // Should throw error
        emoji2SlackService.saveEmojiConfiguration(null,null,0);
    }

    @Test
    public void testGetAllEmojisConfigurations() throws Exception{

        doAnswer(new Answer<Map<String, Emoticon>>() {
            public Map<String, Emoticon> answer(InvocationOnMock invocation) {
                Map<String, Emoticon> map = new HashMap<>();
                map.put("cat",new MockEmoticon("cat","😺"));
                map.put("clap",new MockEmoticon("clap","👏"));
                map.put("question",new MockEmoticon("question","❓"));
                return map;
            }
        }).when(emoticonService).getEmoticons();

        doAnswer(new Answer<EmojiConfigEntity[]>() {
            public EmojiConfigEntity[] answer(InvocationOnMock invocation) {
                EmojiConfigEntity[] tab = new EmojiConfigEntity[3];
                tab[0] = new MockEmojiConfigEntity(0,"channel1","clap",0);
                tab[1] = new MockEmojiConfigEntity(1,"channel2","cat",0);
                tab[2] = new MockEmojiConfigEntity(2,"channel3","not_exist",0);
                return tab;
            }
        }).when(ao).find(any());

        Collection<EmojiConfiguration> result = emoji2SlackService.getAllEmojisConfigurations();

        assertEquals(result.size(),3);

        Iterator<EmojiConfiguration> itr = result.iterator();

        EmojiConfiguration item1 = itr.next();
        assertEquals(item1.getId(),0);
        assertEquals(item1.getEmoji().getValue().get(),"👏");

        EmojiConfiguration item2 = itr.next();
        assertEquals(item2.getId(),1);
        assertEquals(item2.getEmoji().getValue().get(),"😺");

        EmojiConfiguration item3 = itr.next();
        assertEquals(item3.getId(),2);
        assertEquals(item3.getEmoji().getValue().get(),"❓");

    }

}
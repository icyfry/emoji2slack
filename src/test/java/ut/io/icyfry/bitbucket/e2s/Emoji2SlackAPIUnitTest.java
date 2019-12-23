package ut.io.icyfry.bitbucket.e2s;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.core.JsonGenerator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import io.icyfry.bitbucket.e2s.api.EmoticonSerializer;

/**
 * UTs related to the API of Emoji2Slack plugin
 */
public class Emoji2SlackAPIUnitTest {

    @Before
    public void initMocks() {
    }

    @Test
    public void testEmoticonSerializer() throws Exception{

        // Mock JsonGenerator
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                final String result = (String) invocation.getArguments()[0];
                assertEquals("👏",result);
                return null;
            }
        }).when(mockJsonGenerator).writeString(isA(String.class));

        EmoticonSerializer serializer = new  EmoticonSerializer();

        serializer.serialize(new MockEmoticon("clap","👏"),mockJsonGenerator,null);

    }

    
}
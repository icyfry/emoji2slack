package ut.io.icyfry.bitbucket.e2s;

import java.util.Optional;

import com.atlassian.bitbucket.emoticons.Emoticon;

/**
 * Mock implementation of com.atlassian.bitbucket.emoticons.Emoticon
 */
public class MockEmoticon implements Emoticon {

    private String shortcut;
    private String value;

    public MockEmoticon(String shortcut, String value) {
        this.shortcut = shortcut;
        this.value = value;
    }

    @Override
    public String getShortcut() {
        return shortcut;
    }

    @Override
    public  java.util.Optional<java.lang.String> getValue() {
        return Optional.of(value);
    }
    
}
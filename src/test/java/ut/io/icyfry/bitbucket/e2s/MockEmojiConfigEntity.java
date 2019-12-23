package ut.io.icyfry.bitbucket.e2s;

import java.beans.PropertyChangeListener;

import io.icyfry.bitbucket.e2s.data.EmojiConfigEntity;
import net.java.ao.EntityManager;
import net.java.ao.RawEntity;

/**
 * Mock implementation of io.icyfry.bitbucket.e2s.data.EmojiConfigEntity
 */
public class MockEmojiConfigEntity implements EmojiConfigEntity {

    private int id;
    private String channelId;
    private String emojiShortcut;
    private int repositoryId;

    public MockEmojiConfigEntity(int id, String channelId, String emojiShortcut, int repositoryId) {
        this.id = id;
        this.channelId = channelId;
        this.emojiShortcut = emojiShortcut;
        this.repositoryId = repositoryId;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener arg0) {
        // nothing
    }

    @Override
    public EntityManager getEntityManager() {
        return null;
    }

    @Override
    public <X extends RawEntity<Integer>> Class<X> getEntityType() {
        return null;
    }

    @Override
    public void init() {
        // nothing
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener arg0) {
        // nothing
    }

    @Override
    public void save() {
        // nothing
    }

    @Override
    public String getChannelId() {
        return this.channelId;
    }

    @Override
    public void setChannelId(String value) {
        this.channelId = value;
    }

    @Override
    public String getEmojiShortcut() {
        return this.emojiShortcut;
    }

    @Override
    public void setEmojiShortcut(String value) {
        this.emojiShortcut = value;
    }

    @Override
    public int getRepositoryId() {
        return this.repositoryId;
    }

    @Override
    public void setRepositoryId(int value) {
        this.repositoryId = value;
    }

}
package io.icyfry.bitbucket.e2s.data;

import net.java.ao.Entity;
import net.java.ao.schema.Index;
import net.java.ao.schema.Indexes;

/**
 * Configuration element that link a channel to an emoji
 */
@Indexes({
    @Index(name = "id", methodNames = {"getChannelId", "getRepositoryId"})
})
public interface ConfigurationEntity extends Entity
{

    public String getChannelId();
    public void setChannelId(String value);
    
    public String getEmojiShortcut();
    public void setEmojiShortcut(String value);

    public int getRepositoryId();
    public void setRepositoryId(int value);

}
package ut.io.icyfry.bitbucket.e2s;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.atlassian.bitbucket.comment.Comment;
import com.atlassian.bitbucket.comment.CommentOperations;
import com.atlassian.bitbucket.comment.CommentThread;
import com.atlassian.bitbucket.comment.CommentThreadDiffAnchor;
import com.atlassian.bitbucket.property.PropertyMap;
import com.atlassian.bitbucket.task.Task;
import com.atlassian.bitbucket.task.TaskAnchorVisitor;
import com.atlassian.bitbucket.user.ApplicationUser;

/**
 * Mock implementation for UT
 */
public class MockComment implements Comment {

    private String text;

    public MockComment(String text) {
        this.text = text;
	}

	@Override
    public PropertyMap getProperties() {
        return null;
    }

    @Override
    public <T> T accept(TaskAnchorVisitor<T> arg0) {
        return null;
    }

    @Override
    public Optional<CommentThreadDiffAnchor> getAnchor() {
        return null;
    }

    @Override
    public ApplicationUser getAuthor() {
        return null;
    }

    @Override
    public List<Comment> getComments() {
        return null;
    }

    @Override
    public Date getCreatedDate() {
        return null;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public Optional<CommentOperations> getPermittedOperations() {
        return null;
    }

    @Override
    public List<Task> getTasks() {
        return null;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public CommentThread getThread() {
        return null;
    }

    @Override
    public Date getUpdatedDate() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }
    
}
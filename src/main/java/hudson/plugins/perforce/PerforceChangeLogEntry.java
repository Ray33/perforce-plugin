package hudson.plugins.perforce;

import java.util.*;

import org.kohsuke.stapler.export.Exported;

import hudson.scm.*;
import hudson.model.User;

import com.tek42.perforce.model.Changelist;
import java.text.SimpleDateFormat;

/**
 * Perforce Implementation of {@link ChangeLogSet.Entry}.  This is a 1 to 1 mapping of
 * Perforce changelists.
 * <p>
 * Note: Internally, within the plugin we use an actual Perforce Change object in place of this.
 * 
 * @author Mike Wille
 */
public class PerforceChangeLogEntry extends ChangeLogSet.Entry {
    Changelist change;

    public PerforceChangeLogEntry(PerforceChangeLogSet parent) {
        super();
        setParent(parent);
    }
    
    @Override
    public User getAuthor() {
        User author = User.get(change.getUser());
        return author;
    }

    public String getUser() {
        return getAuthor().getDisplayName();
    }

    public Collection<Changelist.FileEntry> getAffectedFiles() {
        return change.getFiles();
    }

    public Collection<String> getAffectedPaths() {
        List<String> paths = new ArrayList<String>(change.getFiles().size());
        for (Changelist.FileEntry entry : change.getFiles()) {
            //only report those files that are actually in the workspace
            if(entry.getWorkspacePath()!=null && !entry.getWorkspacePath().equals("")){
                paths.add(entry.getWorkspacePath());
            }
        }
        return paths;
    }

    @Override
    public String getMsg() {
        return change.getDescription();
    }

    @Exported
    public String getChangeNumber() {
        return new Integer(getChange().getChangeNumber()).toString();
    }

    //used for email-ext
    @Deprecated
    public String getRevision() {
        return getChangeNumber();
    }
    //used for email-ext
    public String getCommitId() {
        return getChangeNumber();
    }

    @Exported
    public String getChangeTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(getChange().getDate());
    }

    //used for email-ext
    @Deprecated
    public String getDate() {
        return getChangeTime();
    }
    //used for email-ext
    public long getTimestamp() {
        return getChange().getDate().getTime();
    }


    /**
     * {@inheritDoc}
     */
    public String getCurrentRevision() {
        return getChangeNumber();
    }

    /**
     * @return the change
     */
    public Changelist getChange() {
        return change;
    }

    /**
     * @param change the change to set
     */
    public void setChange(Changelist change) {
        this.change = change;
    }    
    
}

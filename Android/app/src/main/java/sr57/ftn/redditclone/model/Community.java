package sr57.ftn.redditclone.model;

import java.util.HashSet;
import java.util.Set;

public class Community {
    private Integer community_id;

    private String name;

    private String description;

    private Boolean is_suspended;

    private String suspended_reason;

    private Set<Post> posts = new HashSet<Post>();

    private Set<Flair> flairs = new HashSet<Flair>();

    private Set<Rule> rules = new HashSet<Rule>();

    private Set<Moderator> moderators = new HashSet<Moderator>();

    public Integer getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(Integer community_id) {
        this.community_id = community_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIs_suspended() {
        return is_suspended;
    }

    public void setIs_suspended(Boolean is_suspended) {
        this.is_suspended = is_suspended;
    }

    public String getSuspended_reason() {
        return suspended_reason;
    }

    public void setSuspended_reason(String suspended_reason) {
        this.suspended_reason = suspended_reason;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Flair> getFlairs() {
        return flairs;
    }

    public void setFlairs(Set<Flair> flairs) {
        this.flairs = flairs;
    }

    public Set<Rule> getRules() {
        return rules;
    }

    public void setRules(Set<Rule> rules) {
        this.rules = rules;
    }

    public Set<Moderator> getModerators() {
        return moderators;
    }

    public void setModerators(Set<Moderator> moderators) {
        this.moderators = moderators;
    }


    @Override
    public String toString() {
        return name;
    }
}

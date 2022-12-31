package sr57.ftn.redditclone.model;

import java.util.HashSet;
import java.util.Set;

public class Flair {
    private Integer flair_id;

    private String name;

    private Set<Community> communities = new HashSet<Community>();

    private Set<Post> posts = new HashSet<Post>();

    public Integer getFlair_id() {
        return flair_id;
    }

    public void setFlair_id(Integer flair_id) {
        this.flair_id = flair_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Community> getCommunities() {
        return communities;
    }

    public void setCommunities(Set<Community> communities) {
        this.communities = communities;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
}

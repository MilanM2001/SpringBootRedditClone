package sr57.ftn.redditclone.model;

import sr57.ftn.redditclone.enums.ReactionType;

public class Reaction {
    private Integer reaction_id;

    private ReactionType reaction_type;

    private Comment comment;

    private Post post;

    private User user;

    public Integer getReaction_id() {
        return reaction_id;
    }

    public void setReaction_id(Integer reaction_id) {
        this.reaction_id = reaction_id;
    }

    public ReactionType getReaction_type() {
        return reaction_type;
    }

    public void setReaction_type(ReactionType reaction_type) {
        this.reaction_type = reaction_type;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

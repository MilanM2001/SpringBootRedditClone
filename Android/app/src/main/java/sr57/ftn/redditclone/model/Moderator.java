package sr57.ftn.redditclone.model;

public class Moderator {
    private Integer moderator_id;

    private User user;

    private Community community;

    public Integer getModerator_id() {
        return moderator_id;
    }

    public void setModerator_id(Integer moderator_id) {
        this.moderator_id = moderator_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }
}

package sr57.ftn.redditclone.model.DTO;

public class PostReactionAndroidDTO {
    Integer post_id;

    public PostReactionAndroidDTO(Integer post_id) {
        this.post_id = post_id;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }
}

package sr57.ftn.redditclone.model.DTO;

public class CommentReactionAndroidDTO {
    Integer comment_id;

    public CommentReactionAndroidDTO(Integer comment_id) {
        this.comment_id = comment_id;
    }


    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }
}

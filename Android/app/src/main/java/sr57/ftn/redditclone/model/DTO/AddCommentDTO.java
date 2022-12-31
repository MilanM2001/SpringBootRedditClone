package sr57.ftn.redditclone.model.DTO;

import com.google.gson.annotations.SerializedName;

public class AddCommentDTO {
    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

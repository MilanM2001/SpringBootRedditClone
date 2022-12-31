package sr57.ftn.redditclone.model.DTO;

import com.google.gson.annotations.SerializedName;

public class AddCommunityDTO {

    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;

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
}

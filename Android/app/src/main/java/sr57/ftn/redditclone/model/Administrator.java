package sr57.ftn.redditclone.model;

public class Administrator extends User {

    @Override
    public String getRole() {
        return "ADMIN";
    }

}

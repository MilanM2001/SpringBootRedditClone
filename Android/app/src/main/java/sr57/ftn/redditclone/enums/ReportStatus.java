package sr57.ftn.redditclone.enums;

import androidx.annotation.NonNull;

import java.io.Serializable;

public enum ReportStatus implements Serializable {
    WAITING("Waiting"),
    ACCEPTED("Accepted"),
    DENIED("Denied");

    private final String friendlyName;

    ReportStatus(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @NonNull
    @Override
    public String toString() {
        return friendlyName;
    }
}

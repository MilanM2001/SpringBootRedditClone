package sr57.ftn.redditclone.enums;

import androidx.annotation.NonNull;

import java.io.Serializable;

public enum ReportReason implements Serializable {
    BREAKS_RULES("Breaks Rules"),
    HARASSMENT("Harassment"),
    HATE("Hate"),
    SHARING_PERSONAL_INFORMATION("Sharing Personal Information"),
    IMPERSONATION("Impersonation"),
    COPYRIGHT_VIOLATION("Copyright Violation"),
    TRADEMARK_VIOLATION("Trademark Violation"),
    SPAM("Spam"),
    SELF_HARM_OR_SUICIDE("Self Harm or Suicide"),
    OTHER("Other");

    private final String friendlyName;

    ReportReason(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @NonNull
    @Override
    public String toString() {
        return friendlyName;
    }
}

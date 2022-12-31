package sr57.ftn.reddit.project.model.dto.communityDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuspendCommunityDTO implements Serializable {
    private String suspended_reason;
}

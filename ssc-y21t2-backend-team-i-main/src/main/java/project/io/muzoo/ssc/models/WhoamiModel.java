package project.io.muzoo.ssc.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WhoamiModel {
    private boolean loggedIn = false;
    private String username;
    private String name;
    private String role;
    private Long userId;
}

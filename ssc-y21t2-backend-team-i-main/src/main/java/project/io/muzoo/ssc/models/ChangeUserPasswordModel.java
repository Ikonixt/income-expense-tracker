package project.io.muzoo.ssc.models;

import lombok.Getter;

@Getter
public class ChangeUserPasswordModel {
    int id;
    String username;
    String password;
    String newPassword;
}

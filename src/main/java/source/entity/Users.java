package source.entity;

import java.io.Serializable;

/**
 * Created by Kostya Nirchenko.
 * @since 05.06.2016
 */
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    private int userId;
    private String username;
    private String userPassword;
    private String created_at;
    private String userGroup;

    public Users(String username, String userPassword, String created_at, String userGroup) {
        this.username = username;
        this.userPassword = userPassword;
        this.created_at = created_at;
        this.userGroup = userGroup;
    }

    public Users() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

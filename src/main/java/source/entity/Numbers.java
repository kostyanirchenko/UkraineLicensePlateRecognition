package source.entity;

import java.io.Serializable;

/**
 * Created by Kostya Nirchenko.
 * @since 12.06.2016
 */
public class Numbers implements Serializable {

    private static final long serialVersionUID = 1L;
    private int numberId;
    private String created_at;
    private String screening_at;
    private String ip;
    private String computerName;
    private String user;
    private int screenNumber;

    public Numbers(String created_at, String screening_at, String ip, String computerName, String user, int screenNumber) {
        this.created_at = created_at;
        this.screening_at = screening_at;
        this.ip = ip;
        this.computerName = computerName;
        this.user = user;
        this.screenNumber = screenNumber;
    }

    public Numbers() {}

    public int getNumberId() {
        return numberId;
    }

    public void setNumberId(int numberId) {
        this.numberId = numberId;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getScreening_at() {
        return screening_at;
    }

    public void setScreening_at(String screening_at) {
        this.screening_at = screening_at;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getScreenNumber() {
        return screenNumber;
    }

    public void setScreenNumber(int screenNumber) {
        this.screenNumber = screenNumber;
    }
}

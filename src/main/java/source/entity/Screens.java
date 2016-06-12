package source.entity;

import java.io.Serializable;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 12.06.2016
 */
public class Screens implements Serializable {

    private static final long serialVersionUID = 1L;
    private int screenId;
    private String screenName;

    public Screens(String screenName) {
        this.screenName = screenName;
    }

    public Screens() {
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }
}

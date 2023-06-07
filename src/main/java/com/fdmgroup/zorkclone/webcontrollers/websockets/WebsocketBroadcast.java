package com.fdmgroup.zorkclone.webcontrollers.websockets;

import java.util.ArrayList;
import java.util.List;

public class WebsocketBroadcast {
    private String message;
    private List<String> usernames;

    public WebsocketBroadcast(String message, String... usernames) {
        this.message = message;
        this.usernames = List.of(usernames);
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

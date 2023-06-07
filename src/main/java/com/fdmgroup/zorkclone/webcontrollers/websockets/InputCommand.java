package com.fdmgroup.zorkclone.webcontrollers.websockets;

public class InputCommand {
    private String command;
    private String username;
    private String playername;

    public InputCommand(){}

    public InputCommand(String command, String username, String playername) {
        this.command = command;
        this.username = username;
        this.playername = playername;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    @Override
    public String toString() {
        return "InputCommand{" +
                "command='" + command + '\'' +
                ", username='" + username + '\'' +
                ", playername='" + playername + '\'' +
                '}';
    }
}

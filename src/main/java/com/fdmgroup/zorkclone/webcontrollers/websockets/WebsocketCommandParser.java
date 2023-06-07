package com.fdmgroup.zorkclone.webcontrollers.websockets;

import com.fdmgroup.zorkclone.commands.CommandParser;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class WebsocketCommandParser {

    public String[] parseCommand(String command) {
        String[] commands = {};

        commands = command.split(" ");

        CommandParser parser = new CommandParser();
        String firstCommand = parser.validateFirstCommand(commands);
        if (Objects.isNull(firstCommand) || firstCommand.length() < 1) {
        } else if (command.equals("Bad command")) {
            return null;
        } else {
            return commands;
        }

        return null;
    }
}

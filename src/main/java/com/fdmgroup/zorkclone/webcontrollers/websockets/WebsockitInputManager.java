package com.fdmgroup.zorkclone.webcontrollers.websockets;

import com.fdmgroup.zorkclone.Main;
import com.fdmgroup.zorkclone.commands.CommandProcessor;
import com.fdmgroup.zorkclone.player.Player;
import com.fdmgroup.zorkclone.player.io.PlayerReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class WebsockitInputManager {
    @Autowired
    private WebsocketCommandParser parser;

    @Autowired
    private CommandProcessor processor;

    @Autowired
    private WebsocketOutput output;


    public void handleMessage(InputCommand message) {
        System.out.println("Message from the client: " + message);
        PlayerReader playerReader = Main.getPlayerReader();
        Player player = playerReader.getPlayer(message.getUsername(),message.getPlayername());
        if (message.getCommand().length() > 0) {
            output.broadcastSpecific("<br />" + message.getCommand(), message.getUsername());
            String[] commands = parser.parseCommand(message.getCommand());
            player = processor.processCommand(commands, player);
            if (player == null) {
                return;
            }
        } else {
            output.broadcastSpecific("<br />You didn't enter anything!", message.getUsername());
        }
        playerReader.writePlayer(player);
    }

}

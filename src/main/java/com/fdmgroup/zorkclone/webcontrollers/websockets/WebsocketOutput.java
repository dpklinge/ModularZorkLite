package com.fdmgroup.zorkclone.webcontrollers.websockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebsocketOutput {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    public void broadcastSpecific(String message, String user) {
        List<String> users = new ArrayList<>();
        users.add(user);
        broadcastSpecific(message, users);
    }

    public void broadcastSpecific(String message, List<String> users) {
        System.out.println("Message should be:");
        System.out.println(message);
        System.out.println("to users:");
        System.out.println(users);

        users.forEach(name -> {
            try {
                sendSpecific("<br/>" + message, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("Ending single broadcast.");

    }

    public void broadcastAll(String message) {
        System.out.println("Message should be:");
        System.out.println(message);
        System.out.println("to all");
        sendAll("<br/>" + message);
        System.out.println("Ending single broadcast.");

    }

    public void sendSpecific(String msg, String username) throws Exception {
        simpMessagingTemplate.convertAndSendToUser(
                username, "/response", new ResponseMessage(msg));
    }

    public void sendAll(String msg){
        simpMessagingTemplate.convertAndSend("/all", new ResponseMessage(msg));
    }
}

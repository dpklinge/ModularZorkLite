package com.fdmgroup.zorkclone.webcontrollers.websockets;

import com.fdmgroup.zorkclone.commands.CommandProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    @Autowired
    private CommandProcessor commandProcessor;

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        event.getMessage();
    }
}

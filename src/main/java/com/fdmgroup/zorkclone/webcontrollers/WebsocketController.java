package com.fdmgroup.zorkclone.webcontrollers;

import com.fdmgroup.zorkclone.webcontrollers.websockets.InputCommand;
import com.fdmgroup.zorkclone.webcontrollers.websockets.ResponseMessage;
import com.fdmgroup.zorkclone.webcontrollers.websockets.WebsockitInputManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {
    @Autowired
    private WebsockitInputManager inputManager;

    @MessageMapping("/command")
    public void processCommand(InputCommand command){
        System.out.println("Received command "+command);
        inputManager.handleMessage(command);
    }
}

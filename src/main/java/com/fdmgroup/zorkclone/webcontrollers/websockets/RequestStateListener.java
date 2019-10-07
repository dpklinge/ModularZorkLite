package com.fdmgroup.zorkclone.webcontrollers.websockets;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class RequestStateListener implements ServletRequestListener {

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        System.out.println("Request destroyed");

    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
    	System.out.println("Request Created");
       
    }

}
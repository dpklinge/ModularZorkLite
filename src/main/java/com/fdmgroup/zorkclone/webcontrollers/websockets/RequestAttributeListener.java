package com.fdmgroup.zorkclone.webcontrollers.websockets;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;

/**
 * Application Lifecycle Listener implementation class RequestStateListener
 *
 */
public class RequestAttributeListener implements ServletRequestAttributeListener {

    /**
     * Default constructor. 
     */
    public RequestAttributeListener() {
    }

	/**
     * @see ServletRequestAttributeListener#attributeRemoved(ServletRequestAttributeEvent)
     */
    public void attributeRemoved(ServletRequestAttributeEvent event)  { 
    	String attributeName = event.getName();
		Object attributeValue = event.getValue();
		System.out.println("Request attribute removed : " + attributeName + " : " + attributeValue);
    }

	/**
     * @see ServletRequestAttributeListener#attributeAdded(ServletRequestAttributeEvent)
     */
    public void attributeAdded(ServletRequestAttributeEvent event)  { 
    	String attributeName = event.getName();
		Object attributeValue = event.getValue();
		System.out.println("Request attribute added : " + attributeName + " : " + attributeValue);
    }

	/**
     * @see ServletRequestAttributeListener#attributeReplaced(ServletRequestAttributeEvent)
     */
    public void attributeReplaced(ServletRequestAttributeEvent event)  { 
    	String attributeName = event.getName();
		Object attributeValue = event.getValue();
		System.out.println("Request attribute replaced : " + attributeName + " : " + attributeValue);
    }
	
}

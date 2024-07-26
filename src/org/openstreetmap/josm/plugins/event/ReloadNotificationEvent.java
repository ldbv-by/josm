package org.openstreetmap.josm.plugins.event;

import java.util.EventObject;

/**
 * Creates a NotificationEvent to reload the notifications in the Notification Manager.
 */
public class ReloadNotificationEvent extends EventObject {

    /**
     * Constructor: Creates a NotificationEvent to reload the notifications in the Notification Manager.
     * @param source Fire object to invoke the event method of the listener.
     */
    public ReloadNotificationEvent(Object source) {
        super(source);
    }
}

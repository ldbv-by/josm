package org.openstreetmap.josm.plugins.event;

import java.util.ArrayList;
import java.util.List;

/**
 * The ReloadNotificationEventHandler is used to register listeners and notify the involved listeners
 * as soon as a reload on the NotificationManger is necessary.
 */
public class ReloadNotificationEventHandler {
    private static final ReloadNotificationEventHandler INSTANCE = new ReloadNotificationEventHandler();
    public static ReloadNotificationEventHandler getInstance() {
        return INSTANCE;
    }
    private static final List<ReloadNotificationEventListener> listeners = new ArrayList<>();


    /**
     * Add listener to the ReloadNotificationEventHandler.
     * @param listener New listener.
     */
    public void addCustomEventListener(ReloadNotificationEventListener listener) {
        listeners.add(listener);
    }


    /**
     * Notify all listeners for reload notifications in the Notification Manager.
     * @param source Fire object to invoke the event method of the listener.
     */
    public void fireEvent(Object source) {
        ReloadNotificationEvent event = new ReloadNotificationEvent(source);
        for (ReloadNotificationEventListener listener : listeners) {
            listener.reloadNotification(event);
        }
    }
}

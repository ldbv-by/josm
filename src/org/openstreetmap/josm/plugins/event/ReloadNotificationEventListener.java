package org.openstreetmap.josm.plugins.event;

import java.util.EventListener;

/**
 * Listener that responds to changes as soon as a reload on the NotificationManger is necessary.
 */
public interface ReloadNotificationEventListener extends EventListener {
    void reloadNotification(ReloadNotificationEvent event);
}
